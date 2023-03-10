<%@include file="../init.jsp"%>
<liferay-ui:success key="entryAdded" message="entry-added" />
<liferay-ui:success key="guestbookAdded" message="guestbook-added" />
<liferay-ui:success key="entryDeleted" message="entry-deleted" />

<liferay-portlet:renderURL varImpl="searchURL">
	<portlet:param name="mvcPath"
				   value="/guestbookwebportlet/view_search.jsp" />
</liferay-portlet:renderURL>

<%
	long guestbookId = Long.valueOf((Long) renderRequest
			.getAttribute("guestbookId"));
%>

<portlet:resourceURL id="/exportEntry" var="exportD" />

GuestBook
<aui:input name="backgroundTaskId" value="hrl" type="hidden"/>
<a href="<%=exportD.toString()%>"><clay:icon symbol="download"/></a>
<aui:form action="<%= searchURL %>" name="fm">
	<liferay-portlet:renderURLParams varImpl="searchURL" />

	<div class="row">
		<div class="col-md-8">
			<aui:input inlineLabel="left" label="" name="keywords" placeholder="search-entries" size="256" />
		</div>

		<div class="col-md-4">
			<aui:button type="submit" value="search" />
		</div>
	</div>
</aui:form>


<aui:button-row cssClass="guestbook-buttons">

	<portlet:renderURL var="addEntryURL">
		<portlet:param name="mvcPath" value="/guestbookwebportlet/edit_entry.jsp" />
		<portlet:param name="guestbookId"
					   value="<%=String.valueOf(guestbookId)%>" />
	</portlet:renderURL>

	<aui:button onClick="<%=addEntryURL.toString()%>" value="Add Entry"/>

</aui:button-row>

<liferay-ui:search-container total="<%=EntryLocalServiceUtil.getEntriesCount()%>">
	<liferay-ui:search-container-results
			results="<%=EntryLocalServiceUtil.getEntries(
                   searchContainer.getStart(),
                    searchContainer.getEnd())%>" />

	<liferay-ui:search-container-row
			className="com.liferay.docs.guestbook.model.Entry" modelVar="entry">

		<liferay-ui:search-container-column-text property="message" />

		<liferay-ui:search-container-column-text property="name" />

		<liferay-ui:search-container-column-text property="email" />
		<liferay-ui:search-container-column-jsp
				align="right"
				path="/guestbookwebportlet/entry_actions.jsp" />

	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator />

</liferay-ui:search-container>