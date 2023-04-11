package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

            //JPQL :
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
             */
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(151L, "B");

            em.persist(member1); //insert 쿼리문이 쓰기지연 Sql 저장소에 쌓임
            em.persist(member2); //insert 쿼리문이 쓰기지연 Sql 저장소에 쌓임
            System.out.println("===========================");
            //commit() 시점에 쿼리문이 나간다.
            tx.commit(); //트랜잭션 종료

        }catch (Exception e){
            tx.rollback(); //문제가 생기면 rollback
        }finally {
            em.close();
        }
        emf.close(); //애플리케이션이 종료된다.
    }
}
