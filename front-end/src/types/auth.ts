export type DocumentType = 'CPF' | 'CNPJ'

export type PlanType = 'prepaid' | 'postpaid'

export type AuthRequest = {
  documentId: string
  documentType: DocumentType
}

export type AuthResponse = {
  token: string
  client: {
    id: string
    name: string
    documentId: string
    documentType: DocumentType
    balance?: number
    limit?: number
    planType: PlanType
    active: boolean
  }
}
