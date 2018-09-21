package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Pergunta;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-21T16:24:38")
@StaticMetamodel(Sala.class)
public class Sala_ { 

    public static volatile ListAttribute<Sala, Pergunta> perguntas;
    public static volatile SingularAttribute<Sala, Long> id;
    public static volatile SingularAttribute<Sala, String> descricao;

}