import { Drawer, Grid, Splitter } from 'antd'
import { useMemo, useState } from 'react'
import { ChatWindow } from '../components/chat/ChatWindow'
import { ConversationSidebar } from '../components/chat/ConversationSidebar'
import { useConversationMessages } from '../hooks/useConversationMessages'
import { useConversations } from '../hooks/useConversations'
import { useSendMessage } from '../hooks/useSendMessage'
import type { MessagePriority } from '../types/chat'
import './ChatPage.css'

export function ChatPage() {
  const screens = Grid.useBreakpoint()
  const isMobile = !screens.md
  const [search, setSearch] = useState('')
  const [isDrawerOpen, setIsDrawerOpen] = useState(false)
  const [selectedConversationId, setSelectedConversationId] = useState<string>()
  const conversationsQuery = useConversations()
  const sendMessageMutation = useSendMessage()
  const conversations = conversationsQuery.data ?? []
  const activeConversationId = selectedConversationId ?? conversations[0]?.id
  const messagesQuery = useConversationMessages(activeConversationId)
  const messages = messagesQuery.data ?? []

  const filteredConversations = useMemo(() => {
    const normalizedSearch = search.trim().toLowerCase()

    if (!normalizedSearch) {
      return conversations
    }

    return conversations.filter((conversation) =>
      conversation.recipientName.toLowerCase().includes(normalizedSearch),
    )
  }, [conversations, search])

  const selectedConversation = conversations.find(
    (conversation) => conversation.id === activeConversationId,
  )

  function handleSelectConversation(conversationId: string) {
    setSelectedConversationId(conversationId)
    setIsDrawerOpen(false)
  }

  function handleSendMessage(content: string, priority: MessagePriority) {
    if (!activeConversationId) {
      return
    }

    sendMessageMutation.mutate({
      conversationId: activeConversationId,
      content,
      priority,
    })
  }

  const sidebar = (
    <ConversationSidebar
      conversations={filteredConversations}
      selectedConversationId={activeConversationId}
      search={search}
      isLoading={conversationsQuery.isLoading}
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
          messages={messages}
          isLoading={messagesQuery.isLoading}
          isSending={sendMessageMutation.isPending}
          onOpenConversations={() => setIsDrawerOpen(true)}
          onSendMessage={handleSendMessage}
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
          <ChatWindow
            conversation={selectedConversation}
            messages={messages}
            isLoading={messagesQuery.isLoading}
            isSending={sendMessageMutation.isPending}
            onSendMessage={handleSendMessage}
          />
        </Splitter.Panel>
      </Splitter>
    </main>
  )
}
