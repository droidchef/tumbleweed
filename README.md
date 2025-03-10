# Tumbleweed (early preview)

Effortlessly understand large (and small) Kotlin/Java classes 😎

## Live demo

You can try this [sample interactive graph](https://redgreenio.github.io/) for Signal Android
App's [StoryViewerPageFragment](https://github.com/signalapp/Signal-Android/blob/ff8f9ca81ae6a25e1e946612c817206b9410d9a1/app/src/main/java/org/thoughtcrime/securesms/stories/viewer/page/StoryViewerPageFragment.kt)
class.

## Screenshot

Visualizing [ConversationParentFragment.java](https://github.com/signalapp/Signal-Android/blob/ff8f9ca81ae6a25e1e946612c817206b9410d9a1/app/src/main/java/org/thoughtcrime/securesms/conversation/ConversationParentFragment.java), a **3510 LOC** class from Signal Android App.

![Screenshot](docs/screenshot.png)

## Installation

```bash
$ brew tap redgreenio/tap

$ brew install twd
```

## Updates

```bash
$ brew upgrade twd
```

## Quick start

Build the project first and then run the command line tool.

```bash
$ twd watch io.redgreen.ExampleClass
```

The command will start a web server on port 7070. Go to `localhost:7070` in your browser to see the diagram.

The diagram will be updated in real-time as you make changes to the source code and compile the project.

For more options, run `twd watch --help`.

## Dev commands

Tumbleweed is under development. The current reference for production use is making the tool as accurate
as [jQAssistant](https://jqassistant.org/). Sometimes, we have to process a lot of data to understand the gap between
these two.

The following commands are useful for development and once the project attains version one status, we'll hide these
commands from users but may still continue to use them for developing Tumbleweed.

### json

```bash
$ twd json ExampleClass
```

This command will print the JSON representation of the class to the console. This is the data that is used to render the
[bilevel edge bundling](https://observablehq.com/@d3/bilevel-edge-bundling) graph
on [ObservableHQ](https://observablehq.com).

### view

```bash
$ twd view my-class.json
```

This command will start a web server and visualize the specified JSON file in the browser. It is best used when
comparing a class visualization using Tumbleweed and a JSON file derived for the same class using jQAssistant.

### convert

```bash
$ twd convert jqassistant-query-result.csv
```

Converts class member relationships query result from jQAssistant to JSON format that can be visualized by Tumbleweed.

### diff

```bash
$ twd diff -b jqassistant-graph.json -i tumbleweed-graph.json
```

Compares and prints the differences between a jQAssistant and a Tumbleweed graph.

## Known issues

- Kotlin synthetic functions are also visible in the diagram.

## Licenses

```
Copyright (c) 2022-Present, Ragunath Jawahar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

```
Copyright 2018–2020 Observable, Inc.

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
```
