package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {

    @Id //pk(기본키)를 지정하는 어노테이션
    private Long id;

    private String name;

    //JPA 는 내부적으로 리플렉션과 같은 동적으로 객체를 생성할 때가 있기에 기본생성자가 있어야한다.
    public Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //Getter, Setter..
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
