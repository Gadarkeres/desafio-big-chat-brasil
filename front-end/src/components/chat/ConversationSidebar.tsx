import { InfoCircleOutlined, LogoutOutlined, SearchOutlined, UserOutlined } from '@ant-design/icons'
import { Avatar, Badge, Button, Descriptions, Dropdown, Input, List, Modal, Tag, Typography } from 'antd'
import type { MenuProps } from 'antd'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useStoredClient } from '../../hooks/useStoredClient'
import { clearAuthSession } from '../../services/authStorage'
import type { ConversationSummary } from '../../types/chat'
import { formatCurrency } from '../../utils/currencyFormatter'
import { formatConversationTime } from '../../utils/dateFormatter'

type ConversationSidebarProps = {
  conversations: ConversationSummary[]
  selectedConversationId?: string
  search: string
  isLoading?: boolean
  onSearchChange: (value: string) => void
  onSelectConversation: (conversationId: string) => void
}

export function ConversationSidebar({
  conversations,
  selectedConversationId,
  search,
  isLoading,
  onSearchChange,
  onSelectConversation,
}: ConversationSidebarProps) {
  const navigate = useNavigate()
  const client = useStoredClient()
  const [isInfoModalOpen, setIsInfoModalOpen] = useState(false)

  const menuItems: MenuProps['items'] = [
    {
      key: 'info',
      icon: <InfoCircleOutlined />,
      label: 'Informações da conta',
      onClick: () => setIsInfoModalOpen(true),
    },
    {
      type: 'divider',
    },
    {
      key: 'logout',
      danger: true,
      icon: <LogoutOutlined />,
      label: 'Sair',
      onClick: () => {
        clearAuthSession()
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
        loading={isLoading}
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
                    <small>{formatConversationTime(conversation.lastMessageTime)}</small>
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

      <Modal
        title="Informações da conta"
        open={isInfoModalOpen}
        onCancel={() => setIsInfoModalOpen(false)}
        footer={null}
      >
        {client && (
          <Descriptions
            className="account-info"
            column={1}
            size="small"
            items={[
              {
                key: 'name',
                label: 'Cliente',
                children: client.name,
              },
              {
                key: 'document',
                label: client.documentType,
                children: client.documentId,
              },
              {
                key: 'plan',
                label: 'Plano',
                children: client.planType === 'prepaid' ? 'Pre-pago' : 'Pos-pago',
              },
              {
                key: 'status',
                label: 'Status',
                children: (
                  <Tag color={client.active ? 'green' : 'red'}>
                    {client.active ? 'Ativo' : 'Inativo'}
                  </Tag>
                ),
              },
              {
                key: 'balance',
                label: client.planType === 'prepaid' ? 'Saldo' : 'Limite restante',
                children: formatCurrency(
                  client.planType === 'prepaid' ? client.balance : client.limit,
                ),
              },
            ]}
          />
        )}
      </Modal>
    </aside>
  )
}
