# Projet SpringBoot

## Création du project 
***
### Pré-requis
- java & sdk `v11`
- springboot `v2.6.7`
- Apache Tomcat `v9.0.62`, port `8080`
- intelij community (latest version)
- Postman

## dependence :
- `spring web` pour l'utilisation de springboot en temps de application web qui utilise springMVC et Apache Tomcat
## Exécuter le code 
- Ajout de Main `MyFirstSpringBootApp`
```
@SpringBootApplication
@RestController
public class MyFisrtSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFisrtSpringBootApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "Tout le Monde") String name) {
		return String.format("Salut %s !", name);
	}

}
```
- Ajout de `static/index.html` pour modifier la page d'acceuil
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Mon app Spring</title>
</head>
<body>
    <p><a href="/hello">Salut tout lement</a></p>

    <form action="/hello" method="GET" id="nameForm">
        <div>
            <label for="nameField">Comment l'application doit-elle vous appeler ?</label>
            <input name="myName" id="nameField">
             <button>Salue-moi!</button>
        </div>
    </form>
</body>
</html>
```
- `Exécuter` Apache Tomcat v9.0.62
- Taper l'url `http://localhost:8080/` ou `http://localhost:8080/hello?myName=VotreNom`
---
## Code 
### Ajoutez des dépendances pour JPA et H2 
***
Celles-ci permettent à  l'application Spring de stocker et de récupérer des données relationnelles

###  dans pom.xml
Le module Spring Data JPA prend en charge l'accès aux données à l'aide de l'API Java Persistence (JPA) et
H2 est une base de données SQL en mémoire rapide écrite en Java.
***
- jpa `org.springframework.boot:spring-boot-starter-data-jpa`
- h2 `com.h2database:h2`

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
</dependency>
```
### Créer une entité JPA qui sera stockée dans la base de donnée
Pour définir le modèle de données de l'application Spring, créez une entité JPA. L'application créera et stockera des objets Customer qui ont un identifiant, un prénom et un nom de famille.
***
####  Créez le fichier `Customer.java` 
Customer est un Modèl ou un java beans 
- La classe Customer porte l'annotation `@Entity` de `javax.persistence.Id`  qui indique que la classe `Customer` est une entité JPA qui doit être traduite dans la table correspondante de la base de données
- le champ id porte l'annotation `@Id` de `javax.persistence.Id`  qui indique que c'est une clé de l'objet et l' annotation `@GeneratedValue(strategy = GenerationType.AUTO)` signifiant qqu'elle est généré automatiquement 
```
@Entity
public class Custumer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;
    private String lastName;

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
```
### Créer une interface de référentiel
***
#### Créez le fichier `CustomerRepository.java`

- Ce referentiel étend la l'interface `CrudRepository<T, I>` de `org.springframework.data.repository.CrudRepository;` où T : `Customer` et I : `Integer` type de Id
- Fournit la methode `Custumer findCustomerById(Integer id);`
```
public interface CustomerRepository extends CrudRepository<Custumer, Integer> {
    Custumer findCustomerById(Integer id);
}
```
### Créer un contrôleur Web
***
#### Créez le fichier `DemoController.java`

- l'annotation `@RestController` de `org.springframework.web.bind.annotation.RestController` marque la servlet `DemoController` en tant que gestionnaire de requêtes(un contrôleur REST) 
- l'annotation `@Autowired` de `org.springframework.web.bind.annotation.Autowired`  indique à Spring d'injecter le `customerRepository` bean
- L'annotation `@PostMapping("/add")`  mappe la méthode `addCustomer()` aux requêtes `POST` pour `/add`.
- Les annotations `@RequestParam`  mappent les paramètres de méthode aux paramètres de requête Web correspondants.
- L'annotation `@GetMapping("/vue")`  mappe les méthode du `ripository` aux vue aux requetes `GET`
- L'annotation `@PathVariable`  mappe mappe la valeur à la place de la variable id de l'URL au paramètre de méthode correspondant.

```
@RestController
public class DemoController {
@Autowired
private CustomerRepository customerRepository;

    @PostMapping("/add")
    public String addCustomer(@RequestParam String first, @RequestParam String last){
        Customer customer = new Customer();
        customer.setFirstName(first);
        customer.setLastName(last);
        customerRepository.save(customer);
        return "Ajout d'un nouveau client au ripo!";
    }

    @GetMapping("/list")
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public Customer findCustomerById(@PathVariable Integer id) {
        return customerRepository.findCustomerById(id);
    }


}
```
***
- Exécuter le programme
- `http://localhost:8080/list` affiche un `[]` car on a pas encore ajouter de customer
- Postman
```
POST http://localhost:8080/add?first=Homer&last=Simpson
GET http://localhost:8080/list
GET http://localhost:8080/find/1
```

### Ajouter SprintBoot Actuator
Cela permet à IntelliJ IDEA d'exposer les informations de santé de l'application et tous les mappages de requêtes disponibles au moment de l'exécution.
***
dans pom.xml
- `org.springframework.boot:spring-boot-starter-actuator`
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
```

### Ajouter les DevTools 
IntelliJ IDEA permet de redémarrer uniquement le contexte de votre application Spring sans avoir à redémarrer également tous les contextes de bibliothèque externes. 
Utile pour les grands projets

dans pom.xml
-  Spring Boot DevTools :`org.springframework.boot:spring-boot-devtools`
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```
- Ajouter dans `index.html`
```
    <hr>

    <form action="/add" method="POST">
        <p>Ajouter un client</p>

        <div>
            <label for="firstName">Prenom :</label>
            <input name="first" id="firstName" value="Homer">
        </div>
        <div>
            <label for="lastName">Nom :</label>
            <input name="last" id="lastName" value="Simpson">
        </div>
        <div>
            <button>Ajouter Client</button>
        </div>
    </form>
    <hr>
    <form action="/list" method="GET">
        <button>Liste des Clients</button>
    </form>
```