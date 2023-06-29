<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>
    <#if role == "ADMIN" || role == "MODER">

        <div class="container">
            <h1>Single Feedback</h1>

            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Feedback Title</h5>
                </div>
                <div class="card-body">
                    <p class="card-text"><strong>ID:</strong> ${feedback.id()}</p>
                    <p class="card-text"><strong>Created At:</strong> ${feedback.date()?string("HH:mm:ss dd/MM/yyyy")}</p>
                    <p class="card-text"><strong>Status:</strong> <form method="post" action="/moder/feedback/move">
                        <select name="status">
                            <#list status as s>
                                <option value="${s.name()}"
                                        <#if s.name() == feedback.status()>selected</#if>>${s.name()}</option>
                            </#list>
                        </select>
                        <input type="hidden" name="id" value="${feedback.id()}">
                        <input type="submit" value="move">
                    </form>
                    </p>
                    <p class="card-text">
                        <strong>Assigned To:</strong>
                        <div>
                        <form method="post" action="/moder/feedback/assign?from=single">
                            <select name="moder">
                                <option value="none" selected>none</option>
                                <#list moders as mod>
                                    <option value="${mod.id()}"
                                            <#if mod.name() == feedback.moder()>selected</#if>>${mod.name()}</option>
                                </#list>
                            </select>
                            <input type="hidden" name="id" value="${feedback.id()}">
                            <input type="submit" value="assign">
                        </form>
                        </div>
                    </p>
                    <p class="card-text"><strong>Text:</strong>${feedback.text()}</p>
                    <p class="card-text"><strong>Mentions:</strong><#list feedback.mentions() as m>@${m}<#sep>,</#list></p>
                    <p class="card-text"><strong>Type:</strong> ${feedback.type()}</p>
                    <p class="card-text"><strong>Author:</strong> ${feedback.author()}</p>
                    <p class="card-text"><strong>Review Comment:</strong>
                    <form method="post" action="/moder/feedback/add-comment">
                        <textarea rows="4" cols="50" maxlength="1000"
                                  name="comment"><#if feedback.comment()??>${feedback.comment()}</#if></textarea>
                        <input type="hidden" name="id" value="${feedback.id()}">
                        <input type="submit" value="save comment">
                    </form>
                    </p>
                        <#if feedback.attUrl()?? && !(feedback.attUrl() == '')>
                            <img src="${feedback.attUrl()}" alt="${feedback.attUrl()}" height="300px" width="300px">
                            <a href="${feedback.attUrl()}" target="_blank">original</a>
                    </#if>
                </div>
            </div>

            <div class="mt-4">
                <h5>Change History</h5>
                <ul class="list-group">
                    <#list history as h>
                        <li class="list-group-item">
                            ${h.type()} -
                            <#if h.author()??>${h.author()}</#if> -
                            <#if h.before()??>${h.before()}</#if> -
                            <#if h.after()??>${h.after()}</#if> -
                            <#if h.comment()??>${h.comment()}</#if> -
                            <#if h.time()??>${h.time()?string("HH:mm:ss dd/MM/yyyy")}</#if>
                        </li>
                    </#list>
                </ul>
            </div>
        </div>
    </#if>
</@c.page>