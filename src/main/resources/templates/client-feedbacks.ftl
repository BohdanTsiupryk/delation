<#include "parts/security.ftl">
<#import "parts/security.ftl" as sec>
<#import "parts/common.ftl" as c>


<@c.page>


    <div class="container">
        <h1>Feedback List</h1>

        <ul id="feedbackList" class="list-group">
            <#list feedbacks as f>
                <li class="list-group-item">
                    <div class="card-header">
                        <span class="badge badge-secondary">${f.date()?string("HH:mm:ss dd/MM/yyyy")}</span> ${f.author()}
                    </div>
                    <p class="mb-1">Feedback: ${f.text()}</p>
                    <hr>
                    <#if f.comment()??><p class="mb-1">Comment: ${f.comment()}</p></#if>
                    <small>Submitted by: ${f.moder()}</small>
                </li>
            </#list>
        </ul>
    </div>

</@c.page>