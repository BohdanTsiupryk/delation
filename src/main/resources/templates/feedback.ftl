<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>
    <#if role == "ADMIN" || role == "MODER">

        <ul>
            <li><i>ID</i> - ${feedback.id()}
                <hr>
            </li>

            <li><i>type</i> - <b>${feedback.type()}</b>
                <hr>
            </li>
            <li><i>text</i> - ${feedback.text()}
                <hr>
            </li>
            <li><i>author</i> - ${feedback.author()}
                <hr>
            </li>
            <li><i>assigned</i>:
                <form method="post" action="/moder/feedback/assign">
                    <select name="moder">
                        <#list moders as mod>
                            <option value="none" selected>none</option>
                            <option value="${mod.id()}"
                                    <#if mod.name() == feedback.moder()>selected</#if>>${mod.name()}</option>
                        </#list>
                    </select>
                    <input type="hidden" name="id" value="${feedback.id()}">
                    <input type="submit" value="assign">
                </form>
                <hr>
            </li>
            <li>
                <i>status</i>:
                <form method="post" action="/moder/feedback/move">
                    <select name="status">
                        <#list status as s>
                            <option value="${s.name()}"
                                    <#if s.name() == feedback.status()>selected</#if>>${s.name()}</option>
                        </#list>
                    </select>
                    <input type="hidden" name="id" value="${feedback.id()}">
                    <input type="submit" value="move">
                </form>
                ${feedback.status()}
                <hr>
            </li>
            <li><i>mentions</i>: <br><#list feedback.mentions() as m>@${m}<#sep><br></#list>
                <hr>
            </li>
            <li><i>created</i> - ${feedback.date()?string("HH:mm:ss dd/MM/yyyy")}
                <hr>
            </li>
            <li><i>review comment</i>:
                <form method="post" action="/moder/feedback/add-comment">
                    <textarea rows="4" cols="50"
                              name="comment"><#if feedback.comment()??>${feedback.comment()}</#if></textarea>
                    <input type="hidden" name="id" value="${feedback.id()}">
                    <input type="submit" value="save comment">
                </form>
                <hr>
            </li>
        </ul>


        <ul>
            <#list history as h>
            <li>
                ${h.type()} -
                <#if h.author()??>${h.author()}</#if> -
                <#if h.before()??>${h.before()}</#if> -
                <#if h.after()??>${h.after()}</#if> -
                <#if h.comment()??>${h.comment()}</#if> -
                <#if h.time()??>${h.time()?string("HH:mm:ss dd/MM/yyyy")}</#if>
                </#list>
            </li>
        </ul>
    </#if>
</@c.page>