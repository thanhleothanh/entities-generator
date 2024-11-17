<#macro generate relationship> <#assign referencingField = relationship.getFields()?first>
  <#lt>  @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
  <#lt>  @JoinColumn(name = "${referencingField.getColumnName()}", referencedColumnName = "${referencingField.getReferencedField().getColumnName()}")
  <#lt>  private ${referencingField.getJavaClass()} ${referencingField.getName()};
</#macro>

<#macro generateComp relationship> <#assign entityName = relationship.getFields()[0].getJavaClass()>
  <#lt>  @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
  <#lt>  @JoinColumns({
  <#list relationship.getFields() as referencingField>
    <#lt>    @JoinColumn(name = "${referencingField.getColumnName()}", referencedColumnName = "${referencingField.getReferencedField().getColumnName()}"),
  </#list>
  <#lt>  })
  <#lt>  private ${entityName} ${entityName?uncap_first};
</#macro>