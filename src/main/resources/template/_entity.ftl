package ${entity.getPackageName()};

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Entity
@Table(name = "${entity.getTableName()}")
public class ${entity.getName()} implements Serializable {
  public static final String TABLE_NAME = "${entity.getTableName()}";
  <#--id-->
  <#lt><#if (entity.getIds()?size > 1)>

    <#lt>  @Embeddable
    <#lt>  @Getter
    <#lt>  @Setter
    <#lt>  @NoArgsConstructor
    <#lt>  @AllArgsConstructor
    <#lt>  public static class CompId implements Serializable {
    <#list entity.getIds() as id>
      <#lt>    @Column(name = "${id.getColumnName()}")
      <#lt>    private ${id.getJavaClass()} ${id.getName()};
    </#list>
    <#lt>  }
    <#lt>  @EmbeddedId
    <#lt>  private CompId compId;
  <#lt><#elseif (entity.getIds()?size = 1)> <#assign id = entity.getIds()?first>

    <#lt>  /* define @GeneratedValue, or not */
    <#lt>  @Id
    <#lt>  @Column(name = "${id.getColumnName()}")
    <#lt>  private ${id.getJavaClass()} ${id.getName()};
  </#if>
  <#--normal fields-->
  <#lt><#list entity.getFields() as field>

    <#lt>  @Column(name = "${field.getColumnName()}")
    <#lt>  private ${field.getJavaClass()} ${field.getName()};
  </#list>
  <#--relationships-->
  <#lt><#list entity.getRelationships() as relationship>

    <#lt>  @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    <#lt><#list relationship.getFields() as referencingField>
      <#lt>  @JoinColumn(name = "${referencingField.getColumnName()}", referencedColumnName = "${referencingField.getReferencedField().getColumnName()}")
    </#list>
    <#lt>  private ${relationship.getJavaClass()} ${relationship.getName()};
  </#list>
}
