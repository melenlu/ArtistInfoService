package service.json.model.external;

public final class Pair<L,O> {
    private L link;
    private O object;

    public Pair(L link, O object){
        this.link=link;
        this.object=object;
    }

    public L getLink(){
        return link;
    }
    public O getObject(){
        return object;
    }
}
