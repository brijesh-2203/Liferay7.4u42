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

<%@ page import="com.liferay.asset.kernel.model.AssetCategory" %><%@
page import="com.liferay.asset.kernel.model.AssetVocabulary" %><%@
page import="com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil" %><%@
page import="com.liferay.asset.kernel.service.AssetCategoryServiceUtil" %><%@
page import="com.liferay.asset.kernel.service.AssetVocabularyServiceUtil" %><%@
page import="com.liferay.portlet.asset.util.comparator.AssetVocabularyGroupLocalizedTitleComparator" %>

<%!
public long[] _filterCategoryIds(long vocabularyId, long[] categoryIds) {
	List<Long> filteredCategoryIds = new ArrayList<>();

	for (long categoryId : categoryIds) {
		AssetCategory category = AssetCategoryLocalServiceUtil.fetchCategory(categoryId);

		if (category == null) {
			continue;
		}

		if (category.getVocabularyId() == vocabularyId) {
			filteredCategoryIds.add(category.getCategoryId());
		}
	}

	return ArrayUtil.toArray(filteredCategoryIds.toArray(new Long[0]));
}

private String[] _getCategoryIdsTitles(String categoryIds, String categoryNames, long vocabularyId, ThemeDisplay themeDisplay) {
	if (Validator.isNotNull(categoryIds)) {
		long[] categoryIdsArray = GetterUtil.getLongValues(StringUtil.split(categoryIds));

		if (vocabularyId > 0) {
			categoryIdsArray = _filterCategoryIds(vocabularyId, categoryIdsArray);
		}

		categoryIds = StringPool.BLANK;
		categoryNames = StringPool.BLANK;

		if (categoryIdsArray.length > 0) {
			StringBundler categoryIdsSB = new StringBundler(categoryIdsArray.length * 2);
			StringBundler categoryNamesSB = new StringBundler(categoryIdsArray.length * 2);

			for (long categoryId : categoryIdsArray) {
				AssetCategory category = AssetCategoryLocalServiceUtil.fetchCategory(categoryId);

				if (category == null) {
					continue;
				}

				categoryIdsSB.append(categoryId);
				categoryIdsSB.append(StringPool.COMMA);

				categoryNamesSB.append(category.getTitle(themeDisplay.getLocale()));
				categoryNamesSB.append("_CATEGORY_");
			}

			if (categoryIdsSB.index() > 0) {
				categoryIdsSB.setIndex(categoryIdsSB.index() - 1);
				categoryNamesSB.setIndex(categoryNamesSB.index() - 1);

				categoryIds = categoryIdsSB.toString();
				categoryNames = categoryNamesSB.toString();
			}
		}
	}

	return new String[] {categoryIds, categoryNames};
}
%>