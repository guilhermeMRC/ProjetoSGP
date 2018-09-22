package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Pergunta;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-21T18:21:12")
@StaticMetamodel(Alternativa.class)
public class Alternativa_ { 

    public static volatile SingularAttribute<Alternativa, Pergunta> pergunta;
    public static volatile SingularAttribute<Alternativa, Boolean> correto;
    public static volatile SingularAttribute<Alternativa, Long> id;
    public static volatile SingularAttribute<Alternativa, String> descricao;

}