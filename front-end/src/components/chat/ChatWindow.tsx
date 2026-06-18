import {
  CheckCircleOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined,
  MenuOutlined,
  SendOutlined,
  UserOutlined,
} from '@ant-design/icons'
import { Avatar, Button, Input, Tag, Typography } from 'antd'
import type { ChatMessage, ConversationSummary, MessageStatus } from '../../types/chat'

type ChatWindowProps = {
  conversation?: ConversationSummary
  messages: ChatMessage[]
  onOpenConversations?: () => void
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

export function ChatWindow({ conversation, messages, onOpenConversations }: ChatWindowProps) {
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
        <Typography.Title level={3}>Selecione uma conversa</Typography.Title>
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
          <Typography.Text type="secondary">Destinatario final</Typography.Text>
        </div>
      </header>

      <div className="chat-window__messages">
        {messages.map((message) => {
          const isClient = message.sentBy.type === 'client'

          return (
            <article
              className={isClient ? 'message-bubble message-bubble--client' : 'message-bubble'}
              key={message.id}
            >
              {message.priority === 'urgent' && <Tag color="red">Urgente</Tag>}
              <p>{message.content}</p>
              <footer>
                <span>{message.timestamp}</span>
                <span className={`message-status message-status--${message.status}`}>
                  {statusIcons[message.status]}
                  {statusLabels[message.status]}
                </span>
              </footer>
            </article>
          )
        })}
      </div>

      <footer className="chat-window__composer">
        <Input.TextArea
          autoSize={{ minRows: 1, maxRows: 4 }}
          placeholder="Digite uma mensagem"
        />
        <Button type="primary" shape="circle" icon={<SendOutlined />} />
      </footer>
    </section>
  )
}
