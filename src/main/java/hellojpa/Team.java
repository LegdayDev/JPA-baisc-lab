package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    //양방향 연관관계 설정
    @OneToMany(mappedBy = "team")//Member객체의 변수명과 같이해준다.
    private List<Member> members = new ArrayList<>(); //add시에 nullpointer가 안뜬다.(관례임)

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public List<Member> getMembers() {return members;}

    public void setMembers(List<Member> members) {this.members = members;}
}
