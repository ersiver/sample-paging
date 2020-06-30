# sample-paging
This sample app was built following <b>Google Developers Codelabs</b>. The app displays a list of GitHub repositories. Whenever the user scrolls to the end of the displayed list, a new network request is triggered and its result is displayed on the screen. The app demonstrates the Paging library and its main components:

+ <b>PagingData</b> - a container for paginated data. Each refresh of data has a separate corresponding PagingData. 

+ <b>PagingSource</b>  - the base class for asynchronous loading snapshots of data from a source we define into a stream of PagingData.  

+ <b>PagingDataAdapter</b>  - The UI observes the changed PagingData and uses a PagingDataAdapter to update the RecyclerView that presents the data.

+ <b>RemoteMediator</b>  - to work with the network and database. triggers network requests to fetch additional items when a user scrolls to the end of the list of items stored n DB.

# CheatSheet:

## :link:  PagingSource

#### To build the PagingSource you need to define the following:
+ The type of the paging key 
+ The type of data loaded 
+ Where is the data retrieved from . If data source is specific to a certain query,  make sure you are also passing the query information to.

#### PagingSource requires to implement a `load()` function, this will be called to trigger the async load. The LoadParams object keeps information related to the load operation, including the following:
+ Key of the page to be loaded. If this is the first time that load is called, LoadParams.key will be null. In this case, you will have to define the initial page key. 
+ Load size - the requested number of items to load.

#### The load function returns a LoadResult. LoadResult can take one of the following types:
+ `LoadResult.Page`, if the result was successful.
+ `LoadResult.Error`, in case of error.

## :link: PagingData
To construct the PagingData, we need to decide what API we want to use to pass the PagingData to other layers of our app:
+ Kolin Flow - Pager.flow
+ LiveData - Pager.liveData
+ RxJava Flowable - Pager.flowable
+ RxJava Observable - Pager.observable.

#### PagingConfig
+ This class sets options regarding how to load content from a PagingSource such as how far ahead to load, the size request for the initial load, and others. The only mandatory parameter you have to define is the page size—how many items should be loaded in each page. By default, Paging will keep in memory all the pages you load. To ensure that you're not wasting memory as the user scrolls, set the maxSize parameter in PagingConfig.

## :link: PagingDataAdapter
+ To bind a PagingData to a RecycleView, use a PagingDataAdapter. The PagingDataAdapter gets notified whenever the PagingData content is loaded and then it signals the RecyclerView to update.
+ The PagingDataAdapter listens to internal  loading events as pages are loaded and uses DiffUtil on a background thread to compute fine-grained updates as updated content is received in the form of new PagingData objects.
+ To retry a failed load from the UI, use the PagingDataAdapter.retry method. Under the hood, the Paging library will trigger the `PagingSource.load()` method.
+ To add separators to your list, create a high-level type with separators as one of the supported types. Then use the `PagingData.insertSeparators()` method to implement your separator generation logic.
+ To display the <b>load state as header or footer</b> implement a <b>LoadStateAdapter</b>. This implementation of RecyclerView.Adapter is automatically notified of changes in load state. It makes sure that only <b>Loading and Error</b> states lead to items being displayed and notifies the RecyclerView when an item is removed, inserted, or changed, depending on the LoadState:
  <br>`withLoadStateHeader` - if we only want to display a header—this should be used when your list only supports adding items at the beginning of the list.
  <br>`withLoadStateFooter` - if we only want to display a footer—this should be used when your list only supports adding items at the end of the list.
  <br>`withLoadStateHeaderAndFooter` — if we want to display a header and a footer - if the list can be paged in both directions.
+ If you want to execute other actions based on the load state, use the `PagingDataAdapter.addLoadStateListener()` callback.

#### PagingDataAdapter.addLoadStateListener()
This callback notifies every time there's a change in the load state via a CombinedLoadStates object. CombinedLoadStates allows to get the load state for the 3 different types of load operations:
+ `CombinedLoadStates.refresh` - represents the load state for loading the PagingData for the first time.
+ `CombinedLoadStates.prepend` - represents the load state for loading data at the start of the list.
+ `CombinedLoadStates.append` - represents the load state for loading data at the end of the list.

## :link: RemoteMediator
The Paging library uses the database as a source of truth for the data that needs to be displayed in the UI. Whenever we don't have any more data in the database, we need to request more from the network. To help with this, Paging 3.0 defines the RemoteMediator abstract class, with one method that needs to be implemented: `load()`. This method will be called whenever we need to load more data from the network. This class returns a MediatorResult object, that can either be:
+ Error - if we got an error while requesting data from the network.
+ Success - If we successfully got data from the network. Here, we also need to pass in a signal that tells whether more data can be loaded or not. For example, if the network response was successful but we got an empty list of repositories, it means that there is no more data to be loaded.

#### To be able to build the network request, the load method has 2 parameters that should give us all the information we need:
+ PagingState - this gives us information about the pages that were loaded before, the most recently accessed index in the list, and the PagingConfig we defined when initializing the paging stream.
+ LoadType - this tells us whether we need to load data:
  <br>`LoadType.REFRESH` - if this <b>the first time</b>  we're loading data
  <br>`LoadType.PREPEND` - at the <b>beginning</b> of the data that we previously loaded
  <br>`LoadType.APPEND`  - at the <b>end:</b>

## :link: Preview
+ Displaying a footer based on the load status: (progress spinner or retry button) and separators every 10k stars.
![20200630_093751](https://user-images.githubusercontent.com/58771510/86108707-66871180-babb-11ea-9b47-8522a1cbd0e3.gif)


## :link: Licence
Copyright 2018 Google LLC
