/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.docs.guestbook.service.impl;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.docs.guestbook.model.Entry;
import com.liferay.docs.guestbook.service.base.EntryLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@Component(property = "model.class.name=com.liferay.docs.guestbook.model.Entry", service = AopService.class)
public class EntryLocalServiceImpl extends EntryLocalServiceBaseImpl {

    @Indexable(type = IndexableType.REINDEX)
    public Entry addEntry(long userId, long guestbookId, String name, String email, String message, ServiceContext serviceContext) throws PortalException {

        long groupId = serviceContext.getScopeGroupId();

        User user = userLocalService.getUserById(userId);

        Date now = new Date();

        validate(name, email, message);

        long entryId = counterLocalService.increment();

        Entry entry = entryPersistence.create(entryId);

        entry.setUuid(serviceContext.getUuid());
        entry.setUserId(userId);
        entry.setGroupId(groupId);
        entry.setCompanyId(user.getCompanyId());
        entry.setUserName(user.getFullName());
        entry.setCreateDate(serviceContext.getCreateDate(now));
        entry.setModifiedDate(serviceContext.getModifiedDate(now));
        entry.setExpandoBridgeAttributes(serviceContext);
        entry.setGuestbookId(guestbookId);
        entry.setName(name);
        entry.setEmail(email);
        entry.setMessage(message);

        entryPersistence.update(entry);

        resourceLocalService.addResources(user.getCompanyId(), groupId, userId, Entry.class.getName(), entryId, false, true, true);
        AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId,
                groupId, entry.getCreateDate(), entry.getModifiedDate(),
                Entry.class.getName(), entryId, entry.getUuid(), 0,
                serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(), true, true, null, null, null, null,
                ContentTypes.TEXT_HTML, entry.getName(), null, null, null,
                null, 0, 0, null);

        assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(),
                serviceContext.getAssetLinkEntryIds(),
                AssetLinkConstants.TYPE_RELATED);


        return entry;
    }

    @Indexable(type = IndexableType.REINDEX)
    public Entry updateEntry(long userId, long guestbookId, long entryId, String name, String email, String message, ServiceContext serviceContext) throws PortalException, SystemException {

        Date now = new Date();

        validate(name, email, message);

        Entry entry = getEntry(entryId);

        User user = userLocalService.getUserById(userId);

        entry.setUserId(userId);
        entry.setUserName(user.getFullName());
        entry.setModifiedDate(serviceContext.getModifiedDate(now));
        entry.setName(name);
        entry.setEmail(email);
        entry.setMessage(message);
        entry.setExpandoBridgeAttributes(serviceContext);

        entryPersistence.update(entry);


        resourceLocalService.updateResources(user.getCompanyId(), serviceContext.getScopeGroupId(), Entry.class.getName(), entryId, serviceContext.getModelPermissions());
        AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId,
                serviceContext.getScopeGroupId(),
                entry.getCreateDate(), entry.getModifiedDate(),
                Entry.class.getName(), entryId, entry.getUuid(),
                0, serviceContext.getAssetCategoryIds(),
                serviceContext.getAssetTagNames(), true, true,
                entry.getCreateDate(), null, null, null,
                ContentTypes.TEXT_HTML, entry.getName(), null,
                null, null, null, 0, 0,
                serviceContext.getAssetPriority());

        assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(),
                serviceContext.getAssetLinkEntryIds(),
                AssetLinkConstants.TYPE_RELATED);
        return entry;
    }

    @Indexable(type = IndexableType.DELETE)
    public Entry deleteEntry(long entryId, ServiceContext serviceContext) throws PortalException {

        Entry entry = getEntry(entryId);

        entry = deleteEntry(entryId);

        resourceLocalService.deleteResource(serviceContext.getCompanyId(), Entry.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, entryId);
        AssetEntry assetEntry = assetEntryLocalService.fetchEntry(
                Entry.class.getName(), entryId);

        assetLinkLocalService.deleteLinks(assetEntry.getEntryId());

        assetEntryLocalService.deleteEntry(assetEntry);
        return entry;
    }

    protected void validate(String name, String email, String entry) throws PortalException {

        if (Validator.isNull(name)) {
            //throw new EntryNameException();
        }

        if (!Validator.isEmailAddress(email)) {
            //throw new EntryEmailException();
        }

        if (Validator.isNull(entry)) {
            //throw new EntryMessageException();
        }
    }

    public List<Entry> getEntries(long groupId, long guestbookId) {
        return entryPersistence.findByG_G(groupId, guestbookId);
    }

    public List<Entry> getEntries(long groupId, long guestbookId, int start, int end) throws SystemException {

        return entryPersistence.findByG_G(groupId, guestbookId, start, end);
    }

    public List<Entry> getEntries(long groupId, long guestbookId, int start, int end, OrderByComparator<Entry> obc) {

        return entryPersistence.findByG_G(groupId, guestbookId, start, end, obc);
    }

    public int getEntriesCount(long groupId, long guestbookId) {
        return entryPersistence.countByG_G(groupId, guestbookId);
    }

}