import { Drawer, Grid, Splitter } from 'antd'
import { useMemo, useState } from 'react'
import { ChatWindow } from '../components/chat/ChatWindow'
import { ConversationSidebar } from '../components/chat/ConversationSidebar'
import type { ChatMessage, ConversationSummary } from '../types/chat'
import './ChatPage.css'

const conversations: ConversationSummary[] = []
const messages: ChatMessage[] = []

export function ChatPage() {
  const screens = Grid.useBreakpoint()
  const isMobile = !screens.md
  const [search, setSearch] = useState('')
  const [isDrawerOpen, setIsDrawerOpen] = useState(false)
  const [selectedConversationId, setSelectedConversationId] = useState<string>()

  const filteredConversations = useMemo(() => {
    const normalizedSearch = search.trim().toLowerCase()

    if (!normalizedSearch) {
      return conversations
    }

    return conversations.filter((conversation) =>
      conversation.recipientName.toLowerCase().includes(normalizedSearch),
    )
  }, [search])

  const selectedConversation = conversations.find(
    (conversation) => conversation.id === selectedConversationId,
  )

  const selectedMessages = messages.filter(
    (message) => message.conversationId === selectedConversationId,
  )

  function handleSelectConversation(conversationId: string) {
    setSelectedConversationId(conversationId)
    setIsDrawerOpen(false)
  }

  const sidebar = (
    <ConversationSidebar
      conversations={filteredConversations}
      selectedConversationId={selectedConversationId}
      search={search}
      onSearchChange={setSearch}
      onSelectConversation={handleSelectConversation}
    />
  )

  if (isMobile) {
    return (
      <main className="chat-page chat-page--mobile">
        <Drawer
          className="chat-page__drawer"
          open={isDrawerOpen}
          onClose={() => setIsDrawerOpen(false)}
          placement="left"
          width="min(88vw, 360px)"
          closable={false}
          styles={{ body: { padding: 0 } }}
        >
          {sidebar}
        </Drawer>

        <ChatWindow
          conversation={selectedConversation}
          messages={selectedMessages}
          onOpenConversations={() => setIsDrawerOpen(true)}
        />
      </main>
    )
  }

  return (
    <main className="chat-page">
      <Splitter className="chat-page__splitter">
        <Splitter.Panel defaultSize={360} min={280} max={460}>
          {sidebar}
        </Splitter.Panel>

        <Splitter.Panel min={360}>
          <ChatWindow conversation={selectedConversation} messages={selectedMessages} />
        </Splitter.Panel>
      </Splitter>
    </main>
  )
}
