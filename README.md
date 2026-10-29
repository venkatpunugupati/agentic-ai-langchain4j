# LangChain4j Port of the ReAct Agent
The raw Python ReAct agent rebuilt on LangChain4j.

## Stack

- Java 27, Maven 3.10.0, langchain4j-bom 1.20.0
  (1.19.1 deliberately avoided — the official release notes document a
  wrong-branch incident on that version)
- gpt-4o-mini, temperature 0

## What it is

The same three tools as the original agent, ported as `@Tool` POJOs:

- Calculator (hand-written recursive-descent parser, including the `**`
  operator — no `eval` in the JDK)
- Word count
- Current time

Wiring: `Assistant` interface with `@SystemMessage` / `@MemoryId` /
`@UserMessage`, `OpenAiChatModel`, and `MessageWindowChatMemory` via a
`ChatMemoryProvider`. The theory in one line: annotations declare, the
generated AiServices proxy runs the loop — with function calling, the
reasoning goes latent, unlike the visible Thought/Action text of the raw
ReAct loop.

## File-backed memory

`FileChatMemoryStore`: one JSON file per memory id, via
`ChatMessageSerializer` / `Deserializer`, wired through the
`ChatMemoryProvider`. The `chat-memory/` folder in this repo holds the
proof:

- Run 1 taught the agent the user's name; run 2 in a fresh JVM answered
  "Your name is Venkat" — cross-process memory works.
- Run 3 with a second user id refused — negative control, no leakage.
- The `maxMessages(4)` probe pinned the window at 4 and showed an evicted
  fact is lost with no summarizer to preserve it; restored to
  `maxMessages(10)`. Persistence and eviction are covered here — but that
  is not compaction. The Week 2 rolling-summary `compact_memory` has no
  LangChain4j counterpart.

## Run it

export OPENAI_API_KEY="..."
export MEMORY_ID="user-1"
mvn -q exec:java

## Verified behavior

- Chained word_count → calculator, verified end to end.
- Three-way side-by-side (same question as the Python and Spring AI
  ports): answered 27, with 3 tool calls — including a live
  parallel-call hazard, where `calculate("3")` fired in the same block
  as `countWords` and self-corrected with `calculate("9*3")`.
