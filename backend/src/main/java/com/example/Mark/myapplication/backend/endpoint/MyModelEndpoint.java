package com.example.Mark.myapplication.backend.endpoint;

import com.example.Mark.myapplication.backend.model.MyModel;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "myModelApi",
        version = "v1",
        resource = "myModel",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Mark.example.com",
                ownerName = "backend.myapplication.Mark.example.com",
                packagePath = ""
        )
)
public class MyModelEndpoint {

    private static final Logger logger = Logger.getLogger(MyModelEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(MyModel.class);
    }

    /**
     * Returns the {@link MyModel} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code MyModel} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "myModel/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public MyModel get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting MyModel with ID: " + id);
        MyModel myModel = ofy().load().type(MyModel.class).id(id).now();
        if (myModel == null) {
            throw new NotFoundException("Could not find MyModel with ID: " + id);
        }
        return myModel;
    }

    /**
     * Inserts a new {@code MyModel}.
     */
    @ApiMethod(
            name = "insert",
            path = "myModel",
            httpMethod = ApiMethod.HttpMethod.POST)
    public MyModel insert(MyModel myModel) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that myModel.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(myModel).now();
        logger.info("Created MyModel.");

        return ofy().load().entity(myModel).now();
    }

    /**
     * Updates an existing {@code MyModel}.
     *
     * @param id      the ID of the entity to be updated
     * @param myModel the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code MyModel}
     */
    @ApiMethod(
            name = "update",
            path = "myModel/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public MyModel update(@Named("id") Long id, MyModel myModel) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(myModel).now();
        logger.info("Updated MyModel: " + myModel);
        return ofy().load().entity(myModel).now();
    }

    /**
     * Deletes the specified {@code MyModel}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code MyModel}
     */
    @ApiMethod(
            name = "remove",
            path = "myModel/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(MyModel.class).id(id).now();
        logger.info("Deleted MyModel with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "myModel",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<MyModel> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<MyModel> query = ofy().load().type(MyModel.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<MyModel> queryIterator = query.iterator();
        List<MyModel> myModelList = new ArrayList<MyModel>(limit);
        while (queryIterator.hasNext()) {
            myModelList.add(queryIterator.next());
        }
        return CollectionResponse.<MyModel>builder().setItems(myModelList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(MyModel.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find MyModel with ID: " + id);
        }
    }
}