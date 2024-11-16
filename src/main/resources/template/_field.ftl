<#macro generate field indent>
${indent}@Column(name = "${field.getColumnName()}")
${indent}private ${field.getJavaClass()} ${field.getName()};

</#macro>