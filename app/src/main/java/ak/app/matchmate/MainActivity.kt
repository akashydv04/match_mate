package ak.app.matchmate

import ak.app.matchmate.databinding.ActivityMainBinding
import ak.app.matchmate.ui.main.MainViewModel
import ak.app.matchmate.ui.main.MatchCardAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels() // Delegate to get ViewModel
    private lateinit var matchCardAdapter: MatchCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // Initialize adapter with callbacks that call ViewModel functions
        matchCardAdapter = MatchCardAdapter(
            onAcceptClick = { uuid -> viewModel.acceptUser(uuid) },
            onDeclineClick = { uuid -> viewModel.declineUser(uuid) },
            showImages = true // Initial state, will be updated by observer
        )
        binding.rvMatchCards.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = matchCardAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe match cards list
                launch {
                    viewModel.matchCards.collect { cards ->
                        matchCardAdapter.submitList(cards)
                    }
                }

                // Observe loading state
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                }

                // Observe error messages
                launch {
                    viewModel.errorMessage.collect { message ->
                        if (message != null) {
                            binding.tvErrorMessage.text = message
                            binding.tvErrorMessage.visibility = View.VISIBLE
                        } else {
                            binding.tvErrorMessage.visibility = View.GONE
                        }
                    }
                }

                // Observe image visibility toggle for the extra challenge
                launch {
                    viewModel.showImages.collect { show ->
                        matchCardAdapter.showImages = show
                        matchCardAdapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_images -> {
                viewModel.toggleImageVisibility()
                true
            }
            R.id.action_refresh -> { // Optional: Add a refresh button
                viewModel.refreshMatchCards()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
