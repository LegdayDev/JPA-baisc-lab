package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //EntityManagerFactory는 애플리케이션 로딩시점에 1개만 생성된다.
        //persistence.xml 파일에  persistence-unit name 을 인자로 넣어주면 된다.
        EntityManager em = emf.createEntityManager();
        //비즈니스 로직을 실행하는 트랜잭션 단위는 EntityManager를 사용한다.

        EntityTransaction tx = em.getTransaction();

        tx.begin(); //트랜잭션이 시작

        try {
            /** 객체 저장
             * 객체를 생성하고 persist()메서드로 jpa에 저장

             Member member = new Member();
             member.setId(1L);
             member.setName("Ronaldo");
             em.persist(member); //member객체가 저장된다.
             */

            /** 객체 조회
             * find()메서드에 객체타입과 기본키로 조회한다.

             Member findMember = em.find(Member.class, 1L);
             System.out.println("객체 이름은 : " + findMember.getName());
             */


            /** 객체 삭제
             * 객체를 조회후 삭제한다.
             *
             Member findMember = em.find(Member.class, 1L);
             em.remove(findMember);
             */

            /** 객체수정
             * 객체 Setter메서드로 수정만하면 JPA 가 알아서 수정해준다.

             Member findMember = em.find(Member.class, 1L);
             findMember.setName("Messi");// 기존 이름은 Ronaldo 였다.
             */

            //JPQL
            /**
             * 전체 회원 검색
             *
             List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
             for (Member member : result) {
                System.out.println("member name = " + member.getName());
             }
             */

            /**
             * 회원검색(조건을 주고)
             List<Member> result = em.createQuery("select m from Member m where m.name = :name" , Member.class)
                                                  .setParameter("name","Ronaldo")
                                                  .getResultList();
             for (Member member : result) {
                System.out.println("member name = " + member.getName());
             }
             */

            /**
             * 영속, 비영속 상태확인
             * 실제 persist()시에는 영속성 컨텍스트에 저장
             * commit() 시점에 insert 쿼리문이 DB로 전달
             //비영속 상태
             Member member = new Member();
             member.setId(100L);
             member.setName("HelloJPA");

             //여기서부터 영속 상태
             System.out.println("=== Before ===");
             em.persist(member);
             System.out.println("=== After ===");

             System.out.println("==== commmit 시점에 DB로 SQL문이 내려감 ===");
             */

            /**
             * 1차 캐시에서 객체 조회
             Member member = new Member();
             member.setId(101L);
             member.setName("Son");

             em.persist(member);

             Member findMember = em.find(Member.class, 101L);

             System.out.println("findMember.id = " + findMember.getId());
             System.out.println("findMember.name = " + findMember.getName());
             * 똑같은 객체를 2번 조회하면 쿼리문은 1번만 나간다.
             Member findMember1 = em.find(Member.class, 101L);
             Member findMember2 = em.find(Member.class, 101L);

             System.out.println("result = " + (findMember1 == findMember2));
             */

            /**
             * 쓰기 지연
             Member member1 = new Member(150L, "A");
             Member member2 = new Member(151L, "B");

             em.persist(member1); //insert 쿼리문이 쓰기지연 Sql 저장소에 쌓임
             em.persist(member2); //insert 쿼리문이 쓰기지연 Sql 저장소에 쌓임
             System.out.println("===========================");
             //commit() 시점에 쿼리문이 나간다.
             tx.commit(); //트랜잭션 종료
             */

            /**
             * 변경 감지(Dirty Checking)
             * Java 객체를 수정하면 DB에 있는 실제 데이터도 수정이된다.commit() 시점에
             Member findMember = em.find(Member.class, 150L);
             System.out.println("==== findMember.name 수정 전 ====");
             System.out.println("findMember = " + findMember.getName());
             System.out.println("==== findMember.name 수정 후 ====");
             findMember.setName("zzzzzzzzzzz");
             System.out.println("findMember = " + findMember.getName());
             System.out.println("========================");
             */

            /**
             * 엔티티 삭제(remove)
             Member findMember = em.find(Member.class, 150L);
             em.remove(findMember);

             */

            /**
             * 강제로 flush() 호출
             * 트랜잭션 commit()전에 쿼리문이 날라가는걸 보고싶다면 ?
             Member member = new Member(400L, "member@2400");
             em.persist(member);

             System.out.println("==========flush 호출===========");
             em.flush(); //강제로 flush 호출되서 쿼리문이 db로 날라감
             //commit() 시점에 쿼리문이 나간다.
             System.out.println("============트랜잭션 commit전에 쿼리문 전송");
             tx.commit(); //트랜잭션 종료
             * JPQL 쿼리 실행시 플러시가 자동 호출
             em.persist(memberA);
             em.persist(memberB);
             em.persist(memberC);

             //중간에 JPQL 실행
             query = em.createQuery("select m from Member m", Member.class);
             List<Member> members= query.getResultList();
             *
             */

            /**
             * 준영속 상태
             * detach(entity); 특정 엔티티를 준영속상태
             * clear(); 모든 영속성 컨텍스트를 초기화
             * close(); 영속성 컨텍스트 종료
             Member findMember = em.find(Member.class, 2L); //이때 findMember가 영속상태가 된다.
             findMember.setName("Cristiano");

             em.detach(findMember); //준영속 상태로 전환

             //트랜잭션 커밋을해도 아무일도 일어나지 않는다.
             System.out.println("==================");
             */

            /**
             * EnumType.ORDINAL 을 사용하면 안되는 이유
             * ORDINAL : 숫자로 DB에 들어간다.(enum 값 순서대로)
             * 만약 enum 타입을 추가하면 순서가 꼬인다.. DB에서 확인 불가
             * STRING : enum 타입 이름으로 들어가기 때문에 추가,변경이 되도 인식가능
             Member member = new Member();
             member.setId(3L);
             member.setUsername("Son");
             member.setRoleType(RoleType.ADMIN);

             em.persist(member);
             */

            /**
             * IDENTITY 전략
             * id값은 null상태로 DB에 전송되고 DB에 Insert가 되면 ID값이 정해진다.
             * 그래서 commit()시점이 아닌 persist() 시점에 insert문이 바로 나간다.
             Member member = new Member();
             member.setUsername("Ronaldo");
             System.out.println("==============");
             em.persist(member);
             System.out.println("member의 ID 값 : " + member.getId());
             System.out.println("==============");
             */

            /**
             * SEQUENCE 전략
             * persist() 시점에 DB에 있는 SEQUENCE 에서 Id값을 가져와서 영속성 컨텍스트에 저장한다.
             Member member = new Member();
             member.setUsername("Ronaldo");
             System.out.println("==============");
             em.persist(member);
             System.out.println("member의 ID 값 : " + member.getId());
             System.out.println("==============");
             * 하지만 계속 DB에 있는 SEQUENCE 를 call하면 성능의 문제가 있다.
             * allocationSize 를 통해서 성능개선이 가능하다.
             * allocationSize 의 기본값은 50 이다. 즉, 미리 50의 사이즈를 콜하는거다.
             * hibernate 쿼리문을 보면 MEMBER_SEQ가 2번 호출되었다.
             * 왜냐하면 처음 생성당시에는 DB상에 SEQ는 1이다. 하지만 50씩을 땡겨와야해서 한번더 호출해서 DB SEQ는 51이되고
             * 애플리케이션에서 seq는 1이 되는거다.
             Member member1 = new Member();
             member1.setUsername("Ronaldo");

             Member member2 = new Member();
             member2.setUsername("Messi");

             Member member3 = new Member();
             member3.setUsername("Neymar");

             System.out.println("===============");

             em.persist(member1); //1번 더비호출하고, 2번째 호출시에 DB seq는 51이되고
             em.persist(member2); //여기서부터는 메모리에서 호출하므로 DB 입출력이 없어 성능개선이된다.
             em.persist(member3);// 쭉쭉가다 51번을 만나면 다시 50개를 땡겨와야하므로 DB SEQ가 call이된다.

             System.out.println("member1.getId() = " + member1.getId());
             System.out.println("member2.getId() = " + member2.getId());
             System.out.println("member3.getId() = " + member3.getId());

             System.out.println("=================");
             */

            /**
             * 단방향 연관관계
             //저장
             Team team = new Team();
             team.setName("TeamA");
             em.persist(team);

             Member member = new Member();
             member.setUsername("member1");
             member.setTeam(team); //JPA 가 team의 pk값을 찾아서 등록
             em.persist(member);

             //조회
             Member findMember = em.find(Member.class, member.getId());

             Team findTeam = findMember.getTeam();
             System.out.println("findTeam.getName() : " + findTeam.getName());
             */

            /**
             * 양방향 연관관계
             //저장
             Team team = new Team();
             team.setName("TeamA");
             em.persist(team);

             Member member = new Member();
             member.setUsername("member1");
             member.setTeam(team);
             em.persist(member);

             em.flush(); //쿼리문을 DB에 전송
             em.clear(); //영속성 컨텍스트를 비운다.

             //저장
             Team team = new Team();
             team.setName("TeamA");
             em.persist(team);

             Member member = new Member();
             member.setUsername("member1");
             member.changeTeam(team);
             em.persist(member);

             //            team.getMembers().add(member); //연관관계 편의메서드를 작성하면 안적어도 된다.
             //            em.flush(); //쿼리문을 DB에 전송
             //            em.clear(); //영속성 컨텍스트를 비운다.

             Team findTeam = em.find(Team.class, team.getId());
             List<Member> members = findTeam.getMembers();

             System.out.println("===============");
             for (Member m : members) {
             System.out.println("m.getUsername() = " + m.getUsername());;
             }
             System.out.println("===============");

             */

            /**
             * 상속관계 매핑(조인, 단일테이블, 구현클래스별 테이블)
             * 조인 : 정규화가 잘 되있고 저장공간이 효율
             * 단일테이블 : JPA에서 기본전략, 조인이 없으므로 조회성능이 빠르다
             Movie movie = new Movie();
             movie.setDirector("Ronaldo");
             movie.setActor("Cristiano");

             movie.setName("호날두");
             movie.setPrice(10000);

             em.persist(movie);

             em.flush();
             em.clear();

             Movie findMovie = em.find(Movie.class, movie.getId());
             System.out.println("findMovie = " + findMovie);
             */

            /**
             * 공통속성 처리
             * @MappedSuperclass 어노테이션이 붙은 클래스를 이용하여 공통속성을 따로 빼줄 수 있다.
             */

            /**
             * 프록시
             Member 엔티티 안에 Team 엔티티와 연관관계가 맺어져 있다.
             Member 엔티티를 조회할때 Team 엔티티도 같이 조회가 되는 경우를 막고 싶다. -> 프록시 이용
             * 프록시 객체 확인해보기
             Member member = new Member();
             member.setUsername("Ronaldo");

             em.persist(member);

             em.flush(); //SQL 즉시 전송
             em.clear(); //영속성 컨텍스트 비우기

             Member findMember = em.getReference(Member.class, member.getId());
             System.out.println("findMember = " + findMember.getClass());
             System.out.println("findMember = " + findMember.getId());
             System.out.println("findMember = " + findMember.getUsername());
             *
             * JPA는 객체 비교시 참(true)를 보장해준다.
             * 두 개의 객체중 첫 객체는 find 두번쨰 객체는 getReference로 호출하면 둘다 원본 엔티티로 반환한다.
             * 두 개의 객체중 첫 객체를 getReference 두번째 객체는 find로 호출하면 둘다 프록시 객체로 반환한다.
             *
             * 준영속 상태인 객체를 초기화하면 LazyInitializationException 예외를 일으킨다.
             Member member = new Member();
             member.setUsername("Ronaldo");

             em.persist(member);

             em.flush(); //SQL 즉시 전송
             em.clear(); //영속성 컨텍스트 비우기

             Member refMember = em.getReference(Member.class, member.getId());
             System.out.println("refMember = " + refMember.getClass());

             em.detach(refMember); //준영속 상태로 만든다.

             System.out.println("refMember.getUsername() = " + refMember.getUsername());
             */

            /** 즉시로딩의 문제점
             * 즉시로딩에서 가장 큰 문제는 JPQL 사용시 N+1 문제를 야기한다.
             * JPQL을 사용하면 즉시 번역되서 DB에 쿼리가 전달된다.
             * DB에서 반환값이 만약 Member라면 JPA에서는 Member엔티티에 Team컬럼이 즉시로딩인것을 확인하고 다시 조회한다.
             * 그렇게 되면 쿼리는 1개를 짯는데 추가쿼리가 더 생성되는 N+1 문제가 생긴다.
             Team team1 = new Team();
             team1.setName("ManUtd");
             em.persist(team1);

             Team team2 = new Team();
             team2.setName("ManCity");
             em.persist(team2);

             Member member = new Member();
             member.setUsername("Ronaldo");
             member.setTeam(team1);
             em.persist(member);

             Member member2 = new Member();
             member2.setUsername("Haland");
             member2.setTeam(team2);
             em.persist(member2);

             em.flush();
             em.clear();

             //            Member m = em.find(Member.class, member.getId());
             List<Member> result = em.createQuery("select m from Member m ", Member.class).getResultList();

             System.out.println("====================");
             System.out.println("team1.getName() = " + team1.getName());
             System.out.println("team2.getName() = " + team2.getName());
             System.out.println("====================");
             */

            /** 영속성 전이 : CASCADE
             * 영속성 전이를 사용하지 않을 때
             Child child1 = new Child();
             Child child2 = new Child();

             Parent parent = new Parent();
             parent.addChild(child1);
             parent.addChild(child2);

             em.persist(parent);
             em.persist(child1);
             em.persist(child2);
             * 하지만 cascade = CascadeType.ALL 옵션을 사용하면 한번만 persist해도 자식 엔티티까지 persist해준다
             Child child1 = new Child();
             Child child2 = new Child();

             Parent parent = new Parent();
             parent.addChild(child1);
             parent.addChild(child2);

             em.persist(parent);
             //            em.persist(child1);
             //            em.persist(child2);
             * 고아객체 : 부모엔티티가 사라지면 자식엔티티는 고아가 된다.
             * orphanRemoval = true 옵션을 사용한다.
             Child child1 = new Child();
             Child child2 = new Child();

             Parent parent = new Parent();
             parent.addChild(child1);
             parent.addChild(child2);

             em.persist(parent);

             em.flush();
             em.clear();

             Parent findParent = em.find(Parent.class, parent.getId());
             findParent.getChildList().remove(0);
             System.out.println("==========DELETE 쿼리============");
             */

            /** 임베디드 값 타입
             * 공통된 속성들을 재사용하고 다른 엔티티에서도 사용하기 위해 기본값 타입들을 모아서 새로운 값 타입을 만든다
             Member member = new Member();
             member.setUsername("Ronaldo");
             member.setHomeAddress(new Address("city","street","zipcode"));
             member.setWorkPeriod(new Period());

             em.persist(member);
             */

            /** 객체 타입과 불변 객체
             * 하나의 임베디드 타입을 여러 엔티티가 공유하고 있을때는 임베디드 값 타입을 복사하여 사용하면 된다.
             Address address = new Address("City", "Street", "10000");

             Member member = new Member();
             member.setUsername("Ronaldo");
             member.setHomeAddress(address);
             em.persist(member);

             //부작용을 막기 위해서는 새로운 엔티티를 생성해서 써야한다.
             Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getStreet());
             Member member2 = new Member();
             member2.setUsername("Messi");
             member2.setHomeAddress(copyAddress);
             em.persist(member2);

             member.getHomeAddress().setCity("newCity");
             * 불변객체로 만들면 부작용을 없앨 수 있다.
             * 수정자(setter)를 생성하지 않던가, private로 지정하여 접근불가능하게 만들면된다.
             Address address = new Address("City", "Street", "10000");

             Member member = new Member();
             member.setUsername("Ronaldo");
             member.setHomeAddress(address);
             em.persist(member);

             Member member2 = new Member();
             member2.setUsername("Messi");
             member2.setHomeAddress(address);
             em.persist(member2);

             // address에서 수정자(setter)를 지웠기 때문에 생성자 시점 이후로 수정이 안된다.
             //            member.getHomeAddress().setCity("newCity");
             */

            /** 값 타입 컬렉션
             * 값 타입 컬렉션은 값 타입을 하나 이상 저장할때 사용한다(자바 컬렉션과 똑같다)
             * 관계형 DB에서는 컬렉션을 담아두지 못하므로 별도의 테이블을 생성하여 사용한다.
             * 1) 값 타입 저장 예제
             * 컬렉션에 값을 넣어두고 메인 엔티티만 persist하면 다른 컬렉션들도 insert 쿼리가 나간다.
             Member member = new Member();
             member.setUsername("Ronaldo");
             member.setHomeAddress(new Address("City1", "Street", "100000"));

             member.getFavoriteFoods().add("닭가슴살");
             member.getFavoriteFoods().add("고구마");
             member.getFavoriteFoods().add("브로콜리");

             member.getAddressesHistory().add(new Address("Old1", "Street", "100000"));
             member.getAddressesHistory().add(new Address("Old2", "Street", "100000"));

             em.persist(member);
             * 2) 값 타입 조회 예제
             * 값 타입 조회를 할 때는 기본이 지연로딩이다.
             Member member = new Member();
             member.setUsername("Ronaldo");
             member.setHomeAddress(new Address("City1", "Street", "100000"));

             member.getFavoriteFoods().add("닭가슴살");
             member.getFavoriteFoods().add("고구마");
             member.getFavoriteFoods().add("브로콜리");

             member.getAddressesHistory().add(new Address("Old1", "Street", "100000"));
             member.getAddressesHistory().add(new Address("Old2", "Street", "100000"));

             em.persist(member);

             em.flush();
             em.clear();
             System.out.println("================Strat===============");
             Member findMember = em.find(Member.class, member.getId());
             * 3) 값 타입 수정 예제
             Member member = new Member();
             member.setUsername("Ronaldo");
             member.setHomeAddress(new Address("City1", "Street", "100000"));

             member.getFavoriteFoods().add("닭가슴살");
             member.getFavoriteFoods().add("고구마");
             member.getFavoriteFoods().add("브로콜리");

             member.getAddressesHistory().add(new Address("Old1", "Street", "100000"));
             member.getAddressesHistory().add(new Address("Old2", "Street", "100000"));

             em.persist(member);

             em.flush();
             em.clear();

             System.out.println("================Strat===============");
             Member findMember = em.find(Member.class, member.getId());

             //City1 -> Manchester
             //            findMember.getHomeAddress().setCity("Manchester") -> 잘못된 방법

             //Setter(수정자)를 사용하면 부작용(Side Effect)가 발생하므로 새로운 값을 대입해서 수정해야한다.
             findMember.setHomeAddress(new Address("Manchester","Street","100000"));

             //닭가슴살 -> 부채살
             //컬렉션에 있는 값 타입을 수정할려면 지우고 새로 추가해야한다.
             findMember.getFavoriteFoods().remove("닭가슴살");
             findMember.getFavoriteFoods().add("부채살");

             //old1 -> new1
             //컬렉션마다 다르긴하지만 똑같은 Object를 가져와서 equals를 하기 때문에 그대로 가져와야한다.
             findMember.getAddressesHistory().remove(new Address("Old1", "Street", "100000"));
             findMember.getAddressesHistory().add(new Address("new1", "Street", "100000"));
             */


            /** JPQL 소개
             * where절을 이요한 조회시에는 find() 메서드로 원하는 데이터를 찾을 수 없다.
             *
             List<Member> result = em.createQuery(
             "select m from Member m where m.username like '%Ronaldo%'", Member.class
             ).getResultList();

             for (Member member : result) {
             System.out.println("member = " + member.getUsername());
             }
             * Criteria
             * 자바코드로 JPQL을 사용하여 동적쿼리에 유용함이 있지만 너무 복잡해서 유지보수가 힘들다
             *
             * QueryDSL
             * Criteria 와 마찬가지로 자바코드로 JQPL을 작성한다.
             * 자바코드로 작성하기 때문에 컴파일 시점에 문법오류를 찾을 수 있따.
             * Criteria에 비해 단순하고 쉽기 때문에 실무사용권장 !!!
             */

            tx.commit(); //트랜잭션 종료
        }catch (Exception e){
            tx.rollback(); //문제가 생기면 rollback
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close(); //애플리케이션이 종료된다.
    }

}
