<#macro generate referencingField indent>
${indent}@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
${indent}@JoinColumn(name = "${referencingField.getName()}", referencedColumnName = "${referencingField.getToField().getColumnName()}")
${indent}private ${referencingField.getToField().getJavaClass()} ${referencingField.getName()};

</#macro>