<#assign isAuth = principal??>

<#if isAuth>
    <#assign role = principal.role>

<#else>

    <#assign role = "UNKNOWN">
</#if>