import { LogoutOutlined, SearchOutlined, UserOutlined } from '@ant-design/icons'
import { Avatar, Badge, Button, Dropdown, Input, List, Typography } from 'antd'
import type { MenuProps } from 'antd'
import Cookies from 'js-cookie'
import { useNavigate } from 'react-router-dom'
import { AUTH_TOKEN_KEY } from '../../config/auth'
import type { ConversationSummary } from '../../types/chat'

type ConversationSidebarProps = {
  conversations: ConversationSummary[]
  selectedConversationId?: string
  search: string
  onSearchChange: (value: string) => void
  onSelectConversation: (conversationId: string) => void
}

export function ConversationSidebar({
  conversations,
  selectedConversationId,
  search,
  onSearchChange,
  onSelectConversation,
}: ConversationSidebarProps) {
  const navigate = useNavigate()

  const menuItems: MenuProps['items'] = [
    {
      key: 'logout',
      danger: true,
      icon: <LogoutOutlined />,
      label: 'Sair',
      onClick: () => {
        Cookies.remove(AUTH_TOKEN_KEY)
        navigate('/login', { replace: true })
      },
    },
  ]

  return (
    <aside className="conversation-sidebar">
      <header className="conversation-sidebar__header">
        <div className="conversation-sidebar__topbar">
          <Typography.Title level={2}>Big Chat Brasil</Typography.Title>

          <Dropdown menu={{ items: menuItems }} trigger={['click']} placement="bottomRight">
            <Button shape="circle" icon={<UserOutlined />} aria-label="Menu do usuario" />
          </Dropdown>
        </div>

        <Input
          allowClear
          prefix={<SearchOutlined />}
          placeholder="Buscar por destinatario"
          value={search}
          onChange={(event) => onSearchChange(event.target.value)}
        />
      </header>

      <List
        className="conversation-sidebar__list"
        dataSource={conversations}
        locale={{ emptyText: 'Nenhuma conversa encontrada' }}
        renderItem={(conversation) => {
          const isSelected = conversation.id === selectedConversationId

          return (
            <List.Item
              className={isSelected ? 'conversation-item conversation-item--selected' : 'conversation-item'}
              onClick={() => onSelectConversation(conversation.id)}
            >
              <List.Item.Meta
                avatar={<Avatar size={44} icon={<UserOutlined />} />}
                title={
                  <div className="conversation-item__title">
                    <span>{conversation.recipientName}</span>
                    <small>{conversation.lastMessageTime}</small>
                  </div>
                }
                description={
                  <div className="conversation-item__description">
                    <span>{conversation.lastMessageContent}</span>
                    {conversation.unreadCount > 0 && (
                      <Badge count={conversation.unreadCount} style={{ backgroundColor: '#52c41a' }} />
                    )}
                  </div>
                }
              />
            </List.Item>
          )
        }}
      />
    </aside>
  )
}
