package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
            tx.commit(); //트랜잭션 종료
        }catch (Exception e){
            tx.rollback(); //문제가 생기면 rollback
        }finally {
            em.close();
        }
        emf.close(); //애플리케이션이 종료된다.
    }
}
