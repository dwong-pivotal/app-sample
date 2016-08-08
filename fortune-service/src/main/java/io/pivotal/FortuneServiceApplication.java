package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class FortuneServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FortuneServiceApplication.class, args);
    }
}

@RefreshScope
@RestController
class MessageController {

    @Value("${message:hello}")
    String message;

    @RequestMapping("/message")
    String msg() {
        return this.message;
    }

}

@RestController
class FortuneController {

    @Autowired
    FortuneRepository repository;

    @RequestMapping("/fortunes")
    public Iterable<Fortune> fortunes() {
        return repository.findAll();
    }

    @RequestMapping("/random")
    public Fortune randomFortune() {
        List<Fortune> randomFortunes = repository.randomFortunes(new PageRequest(0, 1));
        return randomFortunes.get(0);
    }
}

@RepositoryRestResource
interface FortuneRepository extends JpaRepository<Fortune, Long> {

    @org.springframework.data.jpa.repository.Query("select fortune from Fortune fortune order by RAND()")
    public List<Fortune> randomFortunes(Pageable pageable);
}

@Entity
@Table(name = "fortunes")
class Fortune {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String text;

    Fortune() { }

    public Fortune(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Fortune{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
