public class Stream {
    String name;
    String location;
    String State;


    public Stream(String name, String location, String State){
        this.name = name;
        this.location = location;
        this.State = State;
    }

    public void displayStream(){
        System.out.println(name + " " + location +  " " +State);
    }
}
