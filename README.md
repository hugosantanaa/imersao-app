# imersao-app
📄 Documentação do Sistema - Imersão App
🌟 Visão Geral
Sistema completo para gerenciamento de usuários e endereços com:
Autenticação segura via JWT
Hierarquia de perfis (Admin/Usuário)
Integração com ViaCEP
CRUD completo com validações
Tratamento de erros centralizado

🚀 Instalação Rápida
Pré-requisitos
Docker e Docker Compose
Java 17
Maven 3.8+

Passos:
Clone o repositório
Execute docker-compose up -d para subir o PostgreSQL
Rode mvn spring-boot:run para iniciar a aplicação

🔐 Fluxo de Autenticação
Registro: Crie uma conta com email e senha
Login: Obtenha seu token JWT
Acesso: Use o token no header Authorization: Bearer <token>

→ Perfis:
Admin: Gerencia todos os usuários
Usuário: Acesso apenas aos próprios dados

📋 Funcionalidades Principais
👤 Módulo de Usuários
Cadastro com validação de email único
Atualização de perfil
Listagem paginada (admin)
Exclusão lógica

🏠 Módulo de Endereços
Cadastro automático via ViaCEP
Associação a usuários
Validação de CEP

🛠️ Testes Automatizados
Cobertura garantida por:
Testes unitários (services)
Testes de integração (controllers)

⚙️ Configurações Técnicas
Banco de dados: PostgreSQL
Segurança: Spring Security + JWT
Monitoramento: Actuator endpoints
