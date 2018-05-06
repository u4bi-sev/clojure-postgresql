## clojure postgresql

### server
```
lein ring server 7776
```

### client(CORS)
```
http://127.0.0.1:5500/client/index.html
```

### API
| URL                       | METHOD |
|---------------------------|--------|
| /user                     | GET    |
| /user/:id                 | GET    |
| /user                     | POST   |
| /user/:id                 | PUT    |
| /user/:id                 | DELETE |

### Table
```sql
CREATE TABLE _user (
    id SERIAL PRIMARY KEY,
    name varchar(32),
    pay double precision,
    age integer
);
```

> requirements
> * ring
> * compojure
> * org.clojure/java.jdbc
> * org.postgresql/postgresql
> * ring-cors
> * axios (client)