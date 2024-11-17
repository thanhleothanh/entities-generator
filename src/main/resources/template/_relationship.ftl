<#macro generate relationship indent> <#assign referencingField = relationship.getFields()?first>
  <#lt>${indent}@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
  <#lt>${indent}@JoinColumn(name = "${referencingField.getColumnName()}", referencedColumnName = "${referencingField.getReferencedField().getColumnName()}")
  <#lt>${indent}private ${referencingField.getJavaClass()} ${referencingField.getName()};

</#macro>

<#macro generateComp relationship indent> <#assign entityName = relationship.getFields()[0].getJavaClass()>
  <#lt>${indent}@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
  <#lt>${indent}@JoinColumns({
  <#list relationship.getFields() as referencingField>
    <#lt>${indent}${indent}@JoinColumn(name = "${referencingField.getColumnName()}", referencedColumnName = "${referencingField.getReferencedField().getColumnName()}"),
  </#list>
  <#lt>${indent}})
  <#lt>${indent}private ${entityName} ${entityName?uncap_first};

</#macro>