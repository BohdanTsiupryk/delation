<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>

        <div class="container">
            <h1>Single Feedback</h1>

            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">Feedback Title</h5>
                </div>
                <div class="card-body">
                    <p class="card-text"><strong>Created At:</strong> ${feedback.date()?string("HH:mm:ss dd/MM/yyyy")}</p>
                    <p class="card-text"><strong>Status:</strong> ${feedback.status()} </p>
                    <p class="card-text">
                        <strong>Assigned To:</strong>
                        ${feedback.moder()}
                    </p>
                    <p class="card-text"><strong>Text:</strong>${feedback.text()}</p>
                    <p class="card-text"><strong>Type:</strong> ${feedback.type()}</p>
                    <p class="card-text"><strong>Author:</strong> ${feedback.author()}</p>
                    <p class="card-text"><strong>Review Comment:</strong>
                        <textarea rows="4" cols="50" maxlength="1000" disabled><#if feedback.comment()??>${feedback.comment()}</#if></textarea>
                    </p>
                        <#if feedback.attUrl()?? && !(feedback.attUrl() == '')>
                            <img src="${feedback.attUrl()}" alt="${feedback.attUrl()}" height="300px" width="300px">
                            <a href="${feedback.attUrl()}" target="_blank">original</a>
                        </#if>
                </div>
            </div>
        </div>
</@c.page>