# video-service

Micro-service **GraphQL** de gestion de vidéos et de leurs créateurs, développé avec **Spring Boot**.
Il expose des requêtes (queries), des mutations et une subscription en temps réel (WebSocket), avec une base **H2** en mémoire.

## Technologies

| Outil | Version / détail |
|---|---|
| Java | 17 minimum (testé avec JDK 23) |
| Spring Boot | 3.2.5 |
| Spring for GraphQL | HTTP + WebSocket |
| Spring Data JPA / Hibernate | 6.4 |
| Base de données | H2 (en mémoire) |
| Lombok | 1.18.36 |
| ModelMapper | 3.2.4 |
| Maven | wrapper inclus (`mvnw`) |

## Démarrage rapide

### Prérequis
- JDK 17 ou plus récent
- (Optionnel) Maven : le wrapper `mvnw` suffit

### Lancer l'application

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Ou, depuis IntelliJ IDEA : exécutez la classe `VideoServiceApplication`.

L'application démarre sur **http://localhost:8090**.


## Points d'accès

| URL | Description |
|---|---|
| `POST http://localhost:8090/graphql` | API GraphQL (requêtes et mutations) |
| `ws://localhost:8090/graphql` | API GraphQL pour les subscriptions (WebSocket) |
| `http://localhost:8090/graphiql.html` | Interface GraphiQL pour tester l'API |
| `http://localhost:8090/h2-console` | Console de la base H2 |

### Console H2
- JDBC URL : `jdbc:h2:mem:video-db`
- Utilisateur : `sa`
- Mot de passe : *(vide)*

### À propos de GraphiQL
La page GraphiQL fournie par Spring Boot (`/graphiql`) charge ses scripts depuis unpkg sans fixer de
version, ce qui provoque une erreur CORS dans le navigateur. Le projet inclut donc sa propre page,
`src/main/resources/static/graphiql.html`, qui utilise des versions fixées via jsDelivr et configure
les subscriptions WebSocket. **Utilisez `/graphiql.html` (avec l'extension).**
Une connexion internet est nécessaire pour charger les scripts depuis le CDN.

## Schéma GraphQL

```graphql
type Video {
    id: Float
    name: String
    url: String
    description: String
    datePublication: String
    creator: Creator
}

type Creator {
    id: Float
    name: String
    email: String
}

type Query {
    videoList: [Video]
    creatorById(id: Float): Creator
}

type Mutation {
    saveVideo(video: VideoRequest): Video
    saveCreator(creator: CreatorRequest): Creator
}

type Subscription {
    notifyVideoChange: Video
}

input VideoRequest {
    name: String
    url: String
    description: String
    datePublication: String
    creator: CreatorRequest
}

input CreatorRequest {
    name: String
    email: String
}
```

## Exemples

### Lister les vidéos

```graphql
{
  videoList {
    id
    name
    url
    datePublication
    creator { name email }
  }
}
```

### Récupérer un créateur

```graphql
{
  creatorById(id: 1) {
    id
    name
    email
  }
}
```

### Créer un créateur

```graphql
mutation {
  saveCreator(creator: { name: "Sara", email: "sara@gmail.com" }) {
    id
    name
    email
  }
}
```

### Créer une vidéo

```graphql
mutation {
  saveVideo(video: {
    name: "GraphQL et Flutter"
    url: "https://example.com/flutter"
    description: "Appeler une API GraphQL depuis Flutter"
    datePublication: "2026-10-01"
    creator: { name: "Sara", email: "sara@gmail.com" }
  }) {
    id
    name
    creator { id name }
  }
}
```

> `saveVideo` enregistre d'abord le créateur fourni, puis la vidéo qui lui est rattachée.

### Subscription

```graphql
subscription {
  notifyVideoChange {
    id
    name
    url
    creator { id name email }
  }
}
```

La subscription est une démonstration : toutes les secondes, elle crée un nouveau créateur
(`Creator-0`, `Creator-1`, ...), l'affecte à la vidéo d'identifiant 1, puis émet la vidéo mise à jour.

### Tester sans GraphiQL (curl)

Avec PowerShell, le plus simple est de passer par un fichier :

```powershell
'{"query":"{ videoList { id name } }"}' | Out-File -Encoding ascii query.json
curl.exe -s -X POST http://localhost:8090/graphql -H "Content-Type: application/json" -d "@query.json"
```

Avec bash :

```bash
curl -X POST http://localhost:8090/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ videoList { id name } }"}'
```

## Structure du projet

```
src/main/java/ma/xproce/videoservice
├── VideoServiceApplication.java    # Point d'entrée + données de test
├── config/                         # Configuration (ModelMapper)
├── dto/                            # VideoRequest, CreatorRequest
├── entities/                       # Video, Creator (JPA)
├── exceptions/                     # GraphQLExceptionHandler
├── managers/                       # Logique métier (VideoManager, CreatorManager)
├── mappers/                        # VideoMapper
├── repositories/                   # Spring Data JPA
└── web/                            # VideoGraphQlController (queries, mutations, subscription)

src/main/resources
├── application.properties
├── graphql/                        # Schémas (.graphqls)
└── static/graphiql.html            # Interface GraphiQL
```

## Configuration

Principaux paramètres de `src/main/resources/application.properties` :

| Propriété | Valeur | Rôle |
|---|---|---|
| `server.port` | `8090` | Port du serveur |
| `spring.datasource.url` | `jdbc:h2:mem:video-db` | Base H2 en mémoire |
| `spring.jpa.hibernate.ddl-auto` | `create` | Schéma recréé à chaque démarrage |
| `spring.h2.console.enabled` | `true` | Active la console H2 |
| `spring.graphql.websocket.path` | `/graphql` | Chemin WebSocket des subscriptions |

> Les données sont perdues à chaque redémarrage (base en mémoire).


