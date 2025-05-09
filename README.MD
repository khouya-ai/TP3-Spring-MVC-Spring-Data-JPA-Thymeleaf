## Activité Pratique N°3 - Spring MVC, Spring Data JPA Thymeleaf
Réalisé par ``Oussama KHOUYA``

L'objectif de cette activité est de créer une page web pour gérer des patients à l'aide du moteur de template Thymeleaf.

## Partie 1 :
### Créer un project springboot avec les bibliothèque Spring Web, Thylemeaf et Spring Data JPA, Mysql, lombok
![img.png](img/img.png)

### Créer l'entité Patient, son repositories et ajouter des patients à la BD
```java
@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Date dateNaissance;
    private boolean malade;
    private int score;
}
// Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {}

// Ajouter des patients au démarrage de l'application
@SpringBootApplication
public class HopitalApplication implements CommandLineRunner {
    
    @Autowired
    private PatientRepository patientRepo;

    @Override
    public void run(String... args) throws Exception {
        patientRepo.save(new Patient(null, "Ali", new Date(), true, 80));
        patientRepo.save(new Patient(null, "Fatima", new Date(), false, 95));
        patientRepo.save(new Patient(null, "Omar", new Date(), true, 60));
    }
    }
```
### Créer un controller pou afficher les patients dans une page HTML
```java
@Controller @AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepo;

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("listPatients", patientRepo.findAll());
        return "patients";
    }
}
```
- Créer un fichier [patients.html](src/main/resources/templates/patients.html) dans resources/templates
```html
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Patients</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css">
</head>
<body>
<h2>List patients</h2>

<table class="table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Nom</th>
        <th>Date</th>
        <th>Malade</th>
        <th>Score</th>
    </tr>
    </thead>
    <tbody>
    <tr th:each="p:${listPatients}">
        <td th:text="${p.id}"></td>
        <td th:text="${p.nom}"></td>
        <td th:text="${p.dateNaissance}"></td>
        <td th:text="${p.malade}"></td>
        <td th:text="${p.score}"></td>
    </tr>
    </tbody>
</table>
</body>
</html>
```
![img_1.png](img/img_1.png)
### Ajouter la pagination au tableau
- On ajoute ma methode **PageRequest.of(page, size)** à notre code
```java
@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepo;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4")int size){
        Page<Patient> pagePatients = patientRepo.findAll(PageRequest.of(page, size));
        model.addAttribute("listPatients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "patients";
    }
}
```
- On ajoute la pagination au-dessous de tableau
```html
<ul class="nav nav-pills">
                <li th:each="page,status:${pages}">
                    <a th:href="@{/index(page=${status.index})}"
                       th:class="${status.index == currentPage} ? 'btn btn-info ms-1' : 'btn btn-outline-info ms-1'"
                       th:text="${status.index}"></a>
                </li>
            </ul>
```
![img_2.png](img/img_2.png)
### Recherher un patient par nom
- On ajoute la method **findPatientsByNomContaining** dans le repository
```java
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findPatientsByNomContaining(String keyword, Pageable pageable);


    @Query("select p from Patient p where p.nom like :x")
    Page<Patient> chercher(@Param("x") String keyword, Pageable pageable);
}
```
- On remplace la methode **findAll** par **findPatientsByNomContaining**
```java
 @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4")int size,
                        @RequestParam(name = "keyword", defaultValue = "")String keyword){
        Page<Patient> pagePatients = patientRepo.findPatientsByNomContaining(keyword, PageRequest.of(page, size));
        model.addAttribute("listPatients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "patients";
    }
```
- On ajoute le formulaire dans la page HTML
```html
<form th:action="@{index}" method="get">
    <label>
        keyword:
        <input type="text" name="keyword" th:value="${keyword}">
    </label>
    <button type="submit" class="btn btn-info">Chercher</button>
</form>
```
![img_3.png](img/img_3.png)

### Supprimer un patient
- On ajoute une méthode pour la suppression
````java
 @GetMapping("/delete")
public String delete(Long id, String keyword, int page){
    patientRepo.deleteById(id);
    return "redirect:/index?page=" + page + "&keyword=" + keyword;
}
````
- On ajoute une boutton delete dans chaque ligne du tableau
````html
  <td>
    <a onclick="javascript:return confirm('Etes vous sure?')"
       th:href="@{delete(id=${p.id}, keyword=${keyword}, page=${currentPage})}"
       class="btn btn-danger">
        <i class="bi bi-trash"></i>
    </a>
</td>
````
![img.png](img/img_4.png)

![img_1.png](img/img_5.png)

## Partie 2 :

### Ajouter une page template
- créer un fichier template1.html
- ajouter la bibiothèque thymeleaf-layout-dialect san pom.xml
- ajouter les names spaces de thyleleaf et layout dans votre template.html : xmlns:th="http://www.thymeleaf.org"
  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
- ajouter la section où votre donneés seront affichées
````html
<section layout:fragment="content1"></section>
````
- ajouter la section avec le contenu dans votre fichier cible
````html
<div layout:fragment="content1">
// Votre contenu exemple : formulaire ou tableau
</div>
````
- Exemple: les deux fichiers patients.html et formPatients utilise le nabar hérité de template1.html
![img_10.png](img/img_10.png)
![img_11.png](img/img_11.png)

### Ajouter la validation pour le Formulaire
- ajouter la bibliothèque spring-boot-starter-validation
- ajouter les annotations de la validation sur les attributs de l'entité
````java
public class Patient{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 5, max = 50)
    private String nom;
    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;
    private boolean malade;
    @DecimalMax("100")
    private int score;
}
````
- ajouter cette balise pou afficher les erreurs sous les champs
````html
<span class="text-danger" th:errors="${patient.nom}"></span>
````
![img_7.png](img/img_7.png)

## Partie 3 : Sécurité avec Spring security
### I - <u>Spring Security avec InMemomy Authentication</u>

pour sécurisé notre application on utilise spring-boot-starter-security

1. On ajoute la dépendance spring security
  ````xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
  ````
2. on ajoute une classe SecuritConfig pour déterminer nos régles de sécurités
````java
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
        String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(encodedPassword).roles("USER").build(),
                User.withUsername("user2").password(encodedPassword).roles("USER").build(),
                User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
        );}
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(ar->ar.requestMatchers("/deletePatient/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .build();
    }

    @Bean
     PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
````
3. On peut accéder au login form avec localhost:8080/login

![img.png](img/img_12.png)
4. pour faire le logout : localhost:8080/logout et clique le button Log Out
![img.png](img/img_13.png)
5. On ajoute des utilisateurs avec des roles différents
```java
  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
      String encodedPassword = passwordEncoder.encode("1234");
      System.out.println(encodedPassword);
      return new InMemoryUserDetailsManager(
              User.withUsername("user1").password(encodedPassword).roles("USER").build(),
              User.withUsername("user2").password(encodedPassword).roles("USER").build(),
              User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
      );}
```

6. On change les path pour configurer les autorisations
```java
@GetMapping("/admin/delete")

@GetMapping("/user/index")

@PostMapping("/admin/save")
```

7. On ajoute la bibliothèque spring-boot-starter-thymeleaf-extras-springsecurity5 pour configurer les springsecurity dans thymeleaf
```xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
    <version>3.1.0.M1</version>
</dependency>
```
8. ajouter les conditions d'affichage dans les fichiers html
```html
<td th:if="${#authorization.expression('hasRole(''ADMIN'')')}">
      <a
         th:href="@{/admin/editPatient(id=${p.id})}"
         class="btn btn-success">
          <i class="bi bi-pen"></i>
      </a>
</td>
```
9. le resultat obtenu est deux differents pages pour chaque role
- page index pour l'utilisateur

![img_15.png](img/img_15.png)

- page index pour l'admin

![img_16.png](img/img_16.png)

- si l'utilisateur essaye d'accéder à une page admin comme http://localhost:8080/admin/editPatient?id=3

![img_14.png](img/img_14.png)

10. personalisé la page login
- changer dans la classe [SecurityConfig.java](src/main/java/khouya/site/Hopital/security/SecurityConfig.java)
```java
httpSecurity.formLogin(form -> form.loginPage("/login").permitAll())
```
- ajouter le path /login dans le controller
```java
  @GetMapping("/login")
public String login() {
  return "login";
}
```
- ajouter une page login.html
```html
<div class="col-md-6 offset-3">
        <div class="card">
            <div class="card-header">
                Authentication
            </div>
            <div class="card-body">
                <form th:action="@{/login}" method="POST">

                    <div class="mb-3">
                        <label for="username" class="form-label">Username:</label>
                        <input type="text" class="form-control" id="username"   name="username"  required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password:</label>
                        <input type="password" class="form-control" id="password"   name="password"  required>
                    </div>
                    <div class="mb-3">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="remember" name="remember-me" value="true">
                            <label class="form-check-label" for="remember">Remeber me:</label>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">Login</button>
                </form>
            </div>
        </div>
    </div>
```
- le resultat est une page de login personalisé

![img.png](img.png)

### II - <u>Spring Security avec JDBC Authentication</u>
Pour ajouter les utilsateurs et les roles dans une base de données on utilise JDBC Authentication

1. Dans SecurityConfig.java on remplace InMemoryUserDetailsManager par JdbcUserDetailsManager
```java
@Bean
    public  JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        // for the data source we will use the same as the global data
        // source (defined in the application.properties file)
        return new JdbcUserDetailsManager(dataSource);
    }
```
2. On ajoute les tables pour les utilisateurs et les roles 
```sql
create table if not exists users(
                                    username varchar(50) not null primary key,
    password varchar(500) not null,
    enabled boolean not null
    );
create table if not exists  authorities (
                                            username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
    );
-- create unique index ix_auth_username on authorities (username,authority);
````
![img_1.png](img/img_18.png)
3. modifier le fichier application.properties pour permettre à spring de créer les tables automatiquement
```properties
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.jpa.generate-ddl=true
```
4. Ajouter des utilisateurs et des roles dans la base de données
```java
 @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){

        return args -> {
            
            if (!jdbcUserDetailsManager.userExists("user1")){
                String encodedPassword = passwordEncoder().encode("1234");
                System.out.println(encodedPassword);
                jdbcUserDetailsManager.createUser(User.withUsername("user1").password(encodedPassword).roles("USER").build());
            }
            if (!jdbcUserDetailsManager.userExists("user2")){
                String encodedPassword = passwordEncoder().encode("1234");
                System.out.println(encodedPassword);
                jdbcUserDetailsManager.createUser(User.withUsername("user2").password(encodedPassword).disabled(true).roles("USER").build());
            }
            if (!jdbcUserDetailsManager.userExists("admin")){
                String encodedPassword = passwordEncoder().encode("1234");
                System.out.println(encodedPassword);
                jdbcUserDetailsManager.createUser(User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build());
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
```

### III - <u>Spring Security avec UserDetailsService Authentication</u>
Pour configurer tous la logique de sécurité dans nos propres classes séparée on utilise UserDetailsService Authentication!
1. Definir des entités [AppUser.java](src/main/java/khouya/site/Hopital/security/entities/AppUser.java) et [AppRole.java](src/main/java/khouya/site/Hopital/security/entities/AppRole.java) <br> ![img_1.png](img/img_17.png)
2. Créer un repository pour chaque entité [AppRoleRepository.java](src/main/java/khouya/site/Hopital/security/repositories/AppRoleRepository.java) et [AppUserRepository.java](src/main/java/khouya/site/Hopital/security/repositories/AppUserRepository.java)
3. Créer une classe [AccountServiceImpl.java](src/main/java/khouya/site/Hopital/security/service/Impl/AccountServiceImpl.java) qui implémente [AccountService.java](src/main/java/khouya/site/Hopital/security/service/AccountService.java)
  ````java
  public interface AccountService {
      AppUser addNewUser(String username, String password, String email, String confirmPassword);
      AppRole addNewRole(String role);
      void addRoleToUser(String username, String role);
      void removeRoleFromUser(String username, String role);
      AppUser loadUserByUsername(String username);
      AppRole loadRole(String role);
  
  }
  ````
4. Ajouter les utilisateurs et les roles dans la base de données, on utilise la méthode CommandLineRunner dans HopitalApplication.java
  ```java
  @Bean
      CommandLineRunner CommandLineRunnerUserDetails(AccountService accountService) {
          return args -> {
              if (accountService.loadRole("USER") == null) {
                  accountService.addNewRole("USER");
              }
              if (accountService.loadRole("ADMIN") == null) {
                  accountService.addNewRole("ADMIN");
              }
              if (accountService.loadUserByUsername("user1") == null) {
                  accountService.addNewUser("user1", "1234", "user1@gmail.com", "1234");
                  accountService.addRoleToUser("user1", "USER");
              }
  
              if (accountService.loadUserByUsername("user2") == null) {
                  accountService.addNewUser("user2", "1234", "user2@gmail.com", "1234");
                  accountService.addRoleToUser("user2", "USER");
              }
  
              if (accountService.loadUserByUsername("admin") == null) {
                  accountService.addNewUser("admin", "1234", "admin@gmail.com", "1234");
                  accountService.addRoleToUser("admin", "USER");
                  accountService.addRoleToUser("admin", "ADMIN");
              }
          };
      }
  ```
5. Créer une classe UserDetailsServiceImpl qui implémente UserDetailsService qui cherche un utilisateur par username
```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  var appUser = accountService.loadUserByUsername(username);
  if (appUser == null) {
    throw new UsernameNotFoundException(String.format("User %s not found", username));
  }
  return User
          .withUsername(appUser.getUsername())
          .password(appUser.getPassword())
          .roles(
                  appUser.getRoles().stream().map(
                          AppRole::getRole
                  ).toArray(String[]::new)
          )
          .build();
}
```
6. modifier Security Filter pour prendre en consideration userDetailsService
````java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
  return httpSecurity
          .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/").permitAll())
          // ...
          .userDetailsService(userDetailsService) // <--- HERE
          .build();
}
````

