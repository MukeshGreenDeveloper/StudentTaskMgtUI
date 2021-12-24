package mukesh.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// An equivalent ViewPager2 adapter class
class ChatPagerAdatper(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InformationFagment()
            1 -> FileFagment()
            else -> GroupChatFagment()
        }
    }
}