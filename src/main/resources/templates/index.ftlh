<#-- @ftlvariable name="membersList" type="java.util.List<ru.dzhiblavi.sd.es.model.Membership>" -->
<#-- @ftlvariable name="curMembershipId" type="java.lang.Long" -->
<#-- @ftlvariable name="currentException" type="java.lang.Exception" -->

<#import "/spring.ftl" as spring/>

<html lang="ru">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
    <link rel="stylesheet" type="text/css"
          href="<@spring.url '/css/normalize.css'/>">
    <link rel="stylesheet" type="text/css"
          href="<@spring.url '/css/style.css'/>">
</head>
<body>

<h3>Membership list</h3>
<table class="table">
    <tr>
        <th>Member id</th>
        <th>Valid until</th>
        <th>Choose</th>
    </tr>
    <#list membersList as item>
        <tr>
            <th>${item.getId()}</th>
            <th>${item.getValidUntil()}</th>
            <th>
                <form method="POST" action="/select-membership">
                    <button class="button" type="submit" value="${item.getId()}"
                            name="identifier"/>
                </form>
            </th>
        </tr>
    </#list>
</table>

<#if curMembershipId != -1>
    <h3>Prolongation of membership with id ${curMembershipId}</h3>
    <form method="POST" action="/prolongate-membership">
        <input type="text" name="duration"/>
        <input type="submit" value="Prolongate membership">
    </form>
</#if>

<h3>Create new membership</h3>
<form method="POST" action="/new-membership">
    <input type="text" name="identifier"/>
    <input type="text" name="valid for"/>
    <input type="submit" value="Create new membership">
</form>

<#if currentException?has_content>
    <h3>Error: ${currentException.getMessage()}</h3>
</#if>

</body>
</html>
