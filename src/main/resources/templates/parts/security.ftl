<#assign isAuth = principal??>
<#assign sameUser = false>

<#if isAuth>
    <#assign role = principal.role>
<#else>

    <#assign role = "UNKNOWN">
</#if>