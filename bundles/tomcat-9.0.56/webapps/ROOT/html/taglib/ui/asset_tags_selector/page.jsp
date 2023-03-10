<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/html/taglib/init.jsp" %>

<%
String addCallback = GetterUtil.getString((String)request.getAttribute("liferay-ui:asset-tags-selector:addCallback"));
boolean allowAddEntry = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:asset-tags-selector:allowAddEntry"));
boolean autoFocus = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:asset-tags-selector:autoFocus"));
String className = (String)request.getAttribute("liferay-ui:asset-tags-selector:className");
long classPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui:asset-tags-selector:classPK"));
String curTags = GetterUtil.getString((String)request.getAttribute("liferay-ui:asset-tags-selector:curTags"));
long[] groupIds = (long[])request.getAttribute("liferay-ui:asset-tags-selector:groupIds");
String hiddenInput = (String)request.getAttribute("liferay-ui:asset-tags-selector:hiddenInput");
String id = GetterUtil.getString((String)request.getAttribute("liferay-ui:asset-tags-selector:id"));
boolean ignoreRequestValue = GetterUtil.getBoolean(request.getAttribute("liferay-ui:asset-tags-selector:ignoreRequestValue"));
String removeCallback = GetterUtil.getString((String)request.getAttribute("liferay-ui:asset-tags-selector:removeCallback"));

if (Validator.isNotNull(className) && (classPK > 0)) {
	curTags = ListUtil.toString(AssetTagServiceUtil.getTags(className, classPK), AssetTag.NAME_ACCESSOR);
}

if (!ignoreRequestValue) {
	String curTagsParam = request.getParameter(hiddenInput);

	if (Validator.isNotNull(curTagsParam)) {
		curTags = curTagsParam;
	}
}
%>

<div class="lfr-tags-selector-content" id="<%= namespace + id %>assetTagsSelector">
	<aui:input name="<%= hiddenInput %>" type="hidden" />

	<c:if test="<%= allowAddEntry %>">
		<input class="form-control lfr-tag-selector-input" id="<%= id %>assetTagNames" maxlength="<%= ModelHintsConstants.TEXT_MAX_LENGTH %>" size="15" title="<liferay-ui:message key="add-tags" />" type="text" />
	</c:if>
</div>

<aui:script use="liferay-asset-tags-selector">
	var assetTagsSelector = new Liferay.AssetTagsSelector(
		{
			allowAddEntry: <%= allowAddEntry %>,
			contentBox: '#<%= namespace + id %>assetTagsSelector',
			curEntries: '<%= HtmlUtil.escapeJS(curTags) %>',

			<c:if test="<%= groupIds != null %>">
				groupIds: '<%= StringUtil.merge(groupIds) %>',
			</c:if>

			hiddenInput: '#<%= namespace + hiddenInput %>',

			<c:if test="<%= allowAddEntry %>">
				input: '#<%= id %>assetTagNames',
			</c:if>

			instanceVar: '<%= namespace + id %>',
			portalModelResource: <%= Validator.isNotNull(className) && (ResourceActionsUtil.isPortalModelResource(className) || className.equals(Group.class.getName())) %>
		}
	).render();

	Liferay.component('<portlet:namespace />tagsSelector', assetTagsSelector);

	<c:if test="<%= Validator.isNotNull(addCallback) %>">
		assetTagsSelector.entries.on(
			'add',
			function(event) {
				window['<portlet:namespace /><%= addCallback %>'](event.item);
			}
		);
	</c:if>

	<c:if test="<%= Validator.isNotNull(removeCallback) %>">
		assetTagsSelector.entries.on(
			'remove',
			function(event) {
				window['<portlet:namespace /><%= removeCallback %>'](event.item);
			}
		);
	</c:if>

	<c:if test="<%= autoFocus %>">
		Liferay.Util.focusFormField('#<%= id %>assetTagNames');
	</c:if>
</aui:script>