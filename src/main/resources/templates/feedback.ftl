<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>
    <#if role == "ADMIN" || role == "MODER">

        <ol>
            <#list list as l>
                <li> ${l} </li>

            </#list>
        </ol>
    </#if>


</@c.page>