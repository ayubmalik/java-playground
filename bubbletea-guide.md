# Bubble Tea: A Practical Guide for Go Developers

Bubble Tea is a Go TUI framework built on the **Elm Architecture** (also called Model-View-Update, or MVU). If you know how Redux works, this will feel familiar. If you don't, the pattern is simple: your entire application state lives in one struct, all changes flow through a single function, and the UI is a pure function of that state.

---

## Core Concepts

### The Three Laws

Every Bubble Tea application implements one interface:

```go
type Model interface {
    Init() tea.Cmd
    Update(tea.Msg) (tea.Model, tea.Cmd)
    View() string
}
```

| Method | When called | Returns |
|--------|-------------|---------|
| `Init()` | Once at startup | An optional initial `Cmd` (or `nil`) |
| `Update(msg)` | On every event | A new model + optional `Cmd` |
| `View()` | After every `Update` | The full UI as a string |

**The golden rule:** `Update` and `View` are pure functions. No side effects — all I/O happens through `Cmd`.

### Messages and Commands

- **`tea.Msg`** — any value; represents an event (key press, timer tick, network result, custom domain event).
- **`tea.Cmd`** — `func() tea.Msg`; a function the runtime runs in a goroutine, then delivers the result as the next `Msg`.

```go
// A Cmd that fires after a delay
func waitOneSecond() tea.Cmd {
    return func() tea.Msg {
        time.Sleep(time.Second)
        return tickMsg{} // returned value becomes the next Msg
    }
}
```

Use `tea.Batch(cmd1, cmd2, ...)` to run multiple commands concurrently from a single `Update` or `Init` return.

---

## Multi-Screen Applications

The canonical pattern for multiple screens is a **screen enum** on the root model. Each screen may have its own sub-model. The root `Update` dispatches to the active screen; the root `View` renders it.

### 1. Define Screens and Messages

```go
package main

import (
    "fmt"
    tea "github.com/charmbracelet/bubbletea"
)

// screen is an enum for the active view.
type screen int

const (
    screenHome screen = iota
    screenDetail
    screenSettings
)

// Domain messages — define all custom Msg types in one place.
type navigateMsg struct{ to screen }
type dataLoadedMsg struct{ content string }
```

### 2. Root Model

The root model holds the active screen and each sub-model:

```go
type rootModel struct {
    screen   screen
    home     homeModel
    detail   detailModel
    settings settingsModel

    width  int
    height int
}

func newRootModel() rootModel {
    return rootModel{
        screen:   screenHome,
        home:     newHomeModel(),
        detail:   newDetailModel(),
        settings: newSettingsModel(),
    }
}
```

### 3. Root Init

Call `Init` on any sub-model that needs startup commands:

```go
func (m rootModel) Init() tea.Cmd {
    return m.home.Init()
}
```

### 4. Root Update — Dispatch Pattern

```go
func (m rootModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
    // Handle global messages first.
    switch msg := msg.(type) {

    case tea.WindowSizeMsg:
        m.width, m.height = msg.Width, msg.Height
        return m, nil

    case tea.KeyMsg:
        if msg.String() == "ctrl+c" {
            return m, tea.Quit
        }

    case navigateMsg:
        m.screen = msg.to
        // Run Init for the screen we're entering.
        switch msg.to {
        case screenDetail:
            return m, m.detail.Init()
        case screenSettings:
            return m, m.settings.Init()
        }
        return m, nil
    }

    // Delegate remaining messages to the active screen.
    switch m.screen {
    case screenHome:
        updated, cmd := m.home.Update(msg)
        m.home = updated.(homeModel)
        return m, cmd
    case screenDetail:
        updated, cmd := m.detail.Update(msg)
        m.detail = updated.(detailModel)
        return m, cmd
    case screenSettings:
        updated, cmd := m.settings.Update(msg)
        m.settings = updated.(settingsModel)
        return m, cmd
    }

    return m, nil
}
```

### 5. Root View — Render the Active Screen

```go
func (m rootModel) View() string {
    switch m.screen {
    case screenHome:
        return m.home.View()
    case screenDetail:
        return m.detail.View()
    case screenSettings:
        return m.settings.View()
    }
    return ""
}
```

---

## Sub-Model Example: Home Screen

Sub-models are normal Go structs that implement `tea.Model`. They navigate by returning a `navigateMsg` as a `Cmd`.

```go
type homeModel struct {
    cursor  int
    choices []string
}

func newHomeModel() homeModel {
    return homeModel{
        choices: []string{"View Detail", "Settings", "Quit"},
    }
}

func (m homeModel) Init() tea.Cmd { return nil }

func (m homeModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
    switch msg := msg.(type) {
    case tea.KeyMsg:
        switch msg.String() {
        case "up", "k":
            if m.cursor > 0 {
                m.cursor--
            }
        case "down", "j":
            if m.cursor < len(m.choices)-1 {
                m.cursor++
            }
        case "enter":
            switch m.cursor {
            case 0:
                // Navigate by emitting a Cmd that returns navigateMsg.
                return m, func() tea.Msg { return navigateMsg{to: screenDetail} }
            case 1:
                return m, func() tea.Msg { return navigateMsg{to: screenSettings} }
            case 2:
                return m, tea.Quit
            }
        }
    }
    return m, nil
}

func (m homeModel) View() string {
    s := "Home\n\n"
    for i, choice := range m.choices {
        cursor := "  "
        if m.cursor == i {
            cursor = "> "
        }
        s += fmt.Sprintf("%s%s\n", cursor, choice)
    }
    s += "\n[↑↓] navigate  [enter] select  [ctrl+c] quit"
    return s
}
```

---

## Sub-Model Example: Detail Screen with Async Loading

This pattern covers the common case of triggering I/O on screen entry and updating state when it completes.

```go
type detailModel struct {
    loading bool
    content string
    err     error
}

func newDetailModel() detailModel {
    return detailModel{loading: true}
}

// Init triggers the async load.
func (m detailModel) Init() tea.Cmd {
    return fetchData()
}

// fetchData is a Cmd — runs in a goroutine, result becomes a Msg.
func fetchData() tea.Cmd {
    return func() tea.Msg {
        // Simulate I/O: database query, HTTP call, file read, etc.
        time.Sleep(500 * time.Millisecond)
        return dataLoadedMsg{content: "Loaded content from somewhere."}
    }
}

func (m detailModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
    switch msg := msg.(type) {
    case dataLoadedMsg:
        m.loading = false
        m.content = msg.content
        return m, nil

    case tea.KeyMsg:
        if msg.String() == "esc" {
            m.loading = true // reset for next visit
            return m, func() tea.Msg { return navigateMsg{to: screenHome} }
        }
    }
    return m, nil
}

func (m detailModel) View() string {
    if m.loading {
        return "\n  Loading…"
    }
    return fmt.Sprintf("Detail\n\n%s\n\n[esc] back", m.content)
}
```

---

## Sub-Model Example: Settings Screen

```go
type settingsModel struct {
    volume int
}

func newSettingsModel() settingsModel { return settingsModel{volume: 50} }
func (m settingsModel) Init() tea.Cmd { return nil }

func (m settingsModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
    switch msg := msg.(type) {
    case tea.KeyMsg:
        switch msg.String() {
        case "left", "h":
            if m.volume > 0 {
                m.volume -= 5
            }
        case "right", "l":
            if m.volume < 100 {
                m.volume += 5
            }
        case "esc":
            return m, func() tea.Msg { return navigateMsg{to: screenHome} }
        }
    }
    return m, nil
}

func (m settingsModel) View() string {
    bar := ""
    for i := 0; i < 20; i++ {
        if i < m.volume/5 {
            bar += "█"
        } else {
            bar += "░"
        }
    }
    return fmt.Sprintf("Settings\n\nVolume: [%s] %d%%\n\n[←→] adjust  [esc] back", bar, m.volume)
}
```

---

## Entry Point

```go
func main() {
    p := tea.NewProgram(
        newRootModel(),
        tea.WithAltScreen(), // use the alternate screen buffer (full-screen TUI)
    )
    if _, err := p.Run(); err != nil {
        fmt.Fprintln(os.Stderr, "error:", err)
        os.Exit(1)
    }
}
```

---

## Key Patterns Summary

### Navigation

Screens navigate by returning a `Cmd` that emits a `navigateMsg`. The root `Update` intercepts it and switches the active screen. This keeps sub-models unaware of each other.

```go
// Inside any sub-model's Update:
return m, func() tea.Msg { return navigateMsg{to: screenFoo} }
```

### Resetting Screen State on Entry

Call the sub-model's `Init()` in the root `Update` when handling `navigateMsg`. This re-triggers loading, resets cursor positions, etc.

### Terminal Size

`tea.WindowSizeMsg` is delivered automatically on startup and on resize. Capture it at the root and pass dimensions down as needed:

```go
case tea.WindowSizeMsg:
    m.width, m.height = msg.Width, msg.Height
    m.detail.width = msg.Width // forward if sub-models need it
    return m, nil
```

### Concurrent Commands

```go
return m, tea.Batch(
    fetchData(),
    startTimer(),
)
```

### Global vs. Local Keys

Handle global keys (quit, help toggle) in the root `Update` **before** delegating to sub-models. Handle screen-local keys inside the sub-model.

---

## Data Flow Diagram

```
User input / I/O
       │
       ▼
  tea.Msg arrives
       │
       ▼
 rootModel.Update(msg)
  ├── global? handle here
  ├── navigateMsg? switch screen
  └── else → delegate to active sub-model.Update(msg)
             └── returns (updatedSubModel, Cmd)
       │
       ▼
 rootModel.View()
  └── active sub-model.View()
       │
       ▼
   Terminal render
```

---

## Common Pitfalls

| Mistake | Fix |
|---------|-----|
| Side effects in `Update` or `View` | Move I/O into a `Cmd` function |
| Forgetting to re-assign the sub-model after `Update` | `m.home = updated.(homeModel)` |
| Navigation logic inside sub-models | Sub-models emit `navigateMsg`; root handles the switch |
| Not resetting sub-model state on re-entry | Call sub-model `Init()` in `navigateMsg` handler |
| Blocking in a `Cmd` | Fine — `Cmd` runs in a goroutine; `Update`/`View` must not block |
