package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Alternativa;
import modelo.Disciplina;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-21T16:24:38")
@StaticMetamodel(Pergunta.class)
public class Pergunta_ { 

    public static volatile ListAttribute<Pergunta, Alternativa> alternativas;
    public static volatile SingularAttribute<Pergunta, Disciplina> disciplina;
    public static volatile SingularAttribute<Pergunta, Integer> tempo;
    public static volatile SingularAttribute<Pergunta, Boolean> habilitar;
    public static volatile SingularAttribute<Pergunta, Long> id;
    public static volatile SingularAttribute<Pergunta, String> tag;
    public static volatile SingularAttribute<Pergunta, Integer> nivel;
    public static volatile SingularAttribute<Pergunta, String> descricao;

}