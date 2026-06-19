import {
  CheckCircleOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined,
  MenuOutlined,
  SendOutlined,
  UserOutlined,
} from '@ant-design/icons'
import { Avatar, Button, Empty, Input, Select, Spin, Tag, Typography } from 'antd'
import { useState } from 'react'
import type {
  ChatMessage,
  ConversationSummary,
  MessagePriority,
  MessageStatus,
} from '../../types/chat'
import { formatMessageTime } from '../../utils/dateFormatter'

type ChatWindowProps = {
  conversation?: ConversationSummary
  messages: ChatMessage[]
  isLoading?: boolean
  isSending?: boolean
  onOpenConversations?: () => void
  onSendMessage?: (content: string, priority: MessagePriority) => void
}

const statusLabels: Record<MessageStatus, string> = {
  queued: 'Enfileirada',
  processing: 'Processando',
  sent: 'Enviada',
  delivered: 'Entregue',
  read: 'Lida',
  failed: 'Falhou',
}

const statusIcons: Partial<Record<MessageStatus, React.ReactNode>> = {
  queued: <ClockCircleOutlined />,
  processing: <ClockCircleOutlined />,
  sent: <CheckCircleOutlined />,
  delivered: <CheckCircleOutlined />,
  read: <CheckCircleOutlined />,
  failed: <ExclamationCircleOutlined />,
}

export function ChatWindow({
  conversation,
  messages,
  isLoading,
  isSending,
  onOpenConversations,
  onSendMessage,
}: ChatWindowProps) {
  const [messageContent, setMessageContent] = useState('')
  const [priority, setPriority] = useState<MessagePriority>('normal')

  function handleSendMessage() {
    const content = messageContent.trim()

    if (!content || !conversation) {
      return
    }

    onSendMessage?.(content, priority)
    setMessageContent('')
    setPriority('normal')
  }

  if (!conversation) {
    return (
      <section className="chat-window chat-window--empty">
        {onOpenConversations && (
          <Button
            className="chat-window__menu-button"
            icon={<MenuOutlined />}
            onClick={onOpenConversations}
          />
        )}
        <Empty description="Selecione uma conversa" />
      </section>
    )
  }

  return (
    <section className="chat-window">
      <header className="chat-window__header">
        {onOpenConversations && (
          <Button
            className="chat-window__menu-button"
            icon={<MenuOutlined />}
            onClick={onOpenConversations}
          />
        )}
        <Avatar size={44} icon={<UserOutlined />} />
        <div>
          <Typography.Title level={4}>{conversation.recipientName}</Typography.Title>
          <Typography.Text type="secondary">Destinatário final</Typography.Text>
        </div>
      </header>

      <div className="chat-window__messages">
        {isLoading ? (
          <Spin className="chat-window__loading" />
        ) : messages.length === 0 ? (
          <Empty className="chat-window__empty-messages" description="Nenhuma mensagem nesta conversa" />
        ) : (
          messages.map((message) => {
            const isClient = message.sentBy.type === 'client'

            return (
              <article
                className={isClient ? 'message-bubble message-bubble--client' : 'message-bubble'}
                key={message.id}
              >
                {message.priority === 'urgent' && <Tag color="red">Urgente</Tag>}
                <p>{message.content}</p>
                <footer>
                  <span>{formatMessageTime(message.timestamp)}</span>
                  <span className={`message-status message-status--${message.status}`}>
                    {statusIcons[message.status]}
                    {statusLabels[message.status]}
                  </span>
                </footer>
              </article>
            )
          })
        )}
      </div>

      <footer className="chat-window__composer">
        <Select<MessagePriority>
          className="chat-window__priority"
          value={priority}
          onChange={setPriority}
          options={[
            { value: 'normal', label: 'Normal' },
            { value: 'urgent', label: 'Urgente' },
          ]}
        />
        <Input.TextArea
          autoSize={{ minRows: 1, maxRows: 4 }}
          placeholder="Digite uma mensagem"
          value={messageContent}
          onChange={(event) => setMessageContent(event.target.value)}
          onPressEnter={(event) => {
            if (!event.shiftKey) {
              event.preventDefault()
              handleSendMessage()
            }
          }}
        />
        <Button
          type="primary"
          shape="circle"
          icon={<SendOutlined />}
          loading={isSending}
          disabled={!messageContent.trim()}
          onClick={handleSendMessage}
        />
      </footer>
    </section>
  )
}
