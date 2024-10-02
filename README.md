## Banken-Service

Dieses Projekt bietet die Funktionalität deutsche Banken zu erfassen und 
bei Eingabe einer IBAN die zugehörige Bank zu ermitteln.  
Eine Validierung der eingegebenen IBAN erfolgt ebenso.

### Backend
Spring-Boot Applikation, welche die in der [openapi.yml](openapi.yml) spezifizierten 
Schnittstellen implementiert.   
Beim ersten Start wird bereits eine Liste deutscher Banken importiert (Quelle https://www.bundesbank.de/).


### Frontend
React basierte Web-Oberfläche, welche
1. eine Übersicht der bereits erfassten Banken darstellt
2. ein Formular zum Erfassen neuer Banken anbietet
3. ein Formular zum Abfragen der Bank zu einer IBAN anbietet

### Build / lokales Ausführen

Für das lokale Ausführen gelten folgende Voraussetzungen:

- docker
- docker-compose
- JDK 21
- Maven
- node 18

```bash
mvn -f banken-backend clean package
npm --prefix banken-frontend ci && npm --prefix banken-frontend run build
docker-compose up --build
```

Die Applikation ist anschließend unter [dieser URL](http://localhost:80/) erreichbar.

### Offene Themen

- Testing (react)
- Authentifizierung (OIDC)
- Betrieb in Cloud / Kubernetes
