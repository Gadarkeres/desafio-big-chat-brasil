export type MessageStatus = 'queued' | 'processing' | 'sent' | 'delivered' | 'read' | 'failed'

export type MessagePriority = 'normal' | 'urgent'

export type ConversationSummary = {
  id: string
  recipientId: string
  recipientName: string
  lastMessageContent: string
  lastMessageTime: string
  unreadCount: number
}

export type ChatMessage = {
  id: string
  conversationId: string
  content: string
  sentBy: {
    id: string
    type: 'client' | 'user'
  }
  timestamp: string
  priority: MessagePriority
  status: MessageStatus
  cost: number
}

export type SendMessageRequest = {
  conversationId: string
  recipientId?: string
  content: string
  priority: MessagePriority
}

export type SendMessageResponse = {
  id: string
  status: 'queued'
  timestamp: string
  estimatedDelivery: string
  cost: number
  currentBalance?: number
}
