package com.ryeslim.senekakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var yesBookmarked: Int = 0//resource id for the filled heart icon
    private var notBookmarked: Int = 0//resource id for the heart contour icon
    private lateinit var thisProverb: Proverb//the quote on the screen
    private lateinit var favorite: ImageView //global ImageView for the two-state favorite (heart) icon, which can be either
    //filled (for bookmarked quotes) or contour (not bookmarked quotes)

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val res = this.resources
        yesBookmarked = res.getIdentifier("ic_favorite", "drawable", this.packageName)
        notBookmarked = res.getIdentifier("ic_favorite_border", "drawable", this.packageName)

        WorkWithProverbs.getInstance().setContext(this)
        instance = this
        AllProverbs.getInstance().setLoadingQue(Volley.newRequestQueue(this))//downloads the list of all quotes

        // Set a "swipe" listener on the quote and react when clicked
        val swipe = SwipeDetector(this)
        theProverb.setOnTouchListener(swipe)

        // Set a click listener on the quote and react when clicked
        theProverb.setOnClickListener { goForward() }

        // Find the '>' ImageView, set a click listener on it and react when clicked
        nextPage.setOnClickListener{ goForward() }

        // Find the '<' ImageView, set a click listener on it and react when clicked
        previousPage.setOnClickListener{ goBackwards() }

        // Find the "first page" ImageView, set a click listener on it and react when clicked
        firstPage.setOnClickListener{ firstPage() }

        // Find the "last page" ImageView, set a click listener on it and react when clicked
        lastPage.setOnClickListener{ lastPage() }

        // Find the "favorite" ImageView, set a click listener on it and react when clicked
        favorite = findViewById(R.id.heart)
        favorite.setOnClickListener {
            if (WorkWithProverbs.getInstance().isBookmarked(thisProverb.theID)) {
                unbookmark()
                favorite.setImageResource(notBookmarked)//switch to the heart contour icon
            } else {
                bookmark()
                favorite.setImageResource(yesBookmarked)//switch to the filled heart icon
            }
        }

        // Find the "show bookmarks" ImageView, set a click listener on it and react when clicked
        showBookmarks.setOnClickListener{ showBookmarks() }

        // Find the "share" ImageView, set a click listener on it and react when clicked
        share.setOnClickListener{ share() }
    }

    fun goForward() {
        thisProverb = WorkWithProverbs.getInstance().theNext
        show(thisProverb)
    }

    fun goBackwards() {
        thisProverb = WorkWithProverbs.getInstance().thePrevious
        show(thisProverb)
    }

    private fun firstPage() {
        thisProverb = WorkWithProverbs.getInstance().theFirst
        show(thisProverb)
    }

    private fun lastPage() {
        thisProverb = WorkWithProverbs.getInstance().theLast
        show(thisProverb)
    }

    private fun bookmark() {
        WorkWithProverbs.getInstance().addToTheFile()//add to the file
        WorkWithProverbs.getInstance().readBookmarks()//update the listOfBookmarks array from the file
    }

    private fun unbookmark() {
        WorkWithProverbs.getInstance().removeFromArray()//remove from listOfBookmarks array
        WorkWithProverbs.getInstance().saveTheUpdatedList()//save to the file
    }

    private fun showBookmarks() {
        val intent = Intent(this, ListOfProverbsActivity::class.java)
        startActivity(intent)
    }

    private fun share() {
        WorkWithProverbs.getInstance().shareSomehow()
    }

    private fun show(thisProverb: Proverb) {

        // Every time a quote is shown, the app reads bookmarks from the file
        // to check if this quote has been bookmarked
        // in order to set the right heart icon
        WorkWithProverbs.getInstance().readBookmarks()

        favorite = findViewById(R.id.heart)//global ImageView

        theProverb.text = thisProverb.proverb

        if (WorkWithProverbs.getInstance().isBookmarked(thisProverb.theID)) {
            favorite.setImageResource(yesBookmarked)//set the filled heart
        } else {
            favorite.setImageResource(notBookmarked)//set the heart contour
        }
    }

    companion object {

        var instance: MainActivity? = null
            private set
    }
}

