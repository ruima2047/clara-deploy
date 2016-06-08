import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-6-1
 * Time: 下午5:34
 * To change this template use File | Settings | File Templates.
 */
public class Main {
//    public static void main(String []args) {
//        Vector personList = new Vector(2);
//        Student student = new Student();
//        Teacher teacher = new Teacher();
//        personList.addElement(student);
//        personList.addElement(teacher);
//
//        Person person1 = (Person)personList.get(0);
//        Person person2 = (Person)personList.get(1);
//        person1.hi();
//        person2.hi();
//    }
public static void main(String []args) {

    Manager m = new Manager("ma");
    System.out.println(m.age);
    }
}
