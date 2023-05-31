package hellojpa;

public class ValueMain {
    public static void main(String[] args) {

        int a = 10;
        int b = 10;

        System.out.println("a == b : "+ (a==b));

        Address address1 = new Address("city", "street", "10000");
        Address address2 = new Address("city", "street", "10000");

        // 동일성(identity) 비교 : 인스턴스의 참조 값을 비교 , == 연산자
        System.out.println("address1 == address2 : " + (address1==address2));
        // 동등성(equivalence) 비교 : 인스턴스의 값을 비교, equlas()사용
        // equals() 메서드는 비교하는 클래스에서 재정의하여 사용해햐 한다.
        System.out.println("address1 equals address2 : " + (address1.equals(address2)));
    }
}
