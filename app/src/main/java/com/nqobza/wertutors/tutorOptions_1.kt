package com.nqobza.wetutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nqobza.wertutors.R
import com.nqobza.wertutors.Tutor
import com.nqobza.wertutors.TutorAdapter
import com.nqobza.wertutors.TutorExpandedActivity

private lateinit var recyclerView: RecyclerView
private lateinit var tutorList: ArrayList<Tutor>
lateinit var imageId: Array<Int>
lateinit var name: Array<String>
lateinit var rating: Array<Double>
lateinit var rate: Array<Double>
lateinit var location: Array<String>
lateinit var languages: Array<String>
lateinit var description: Array<String>
lateinit var aboutTutor: Array<String>

class tutorOptions_1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tutor_options_1, container, false)

        recyclerView = view.findViewById(R.id.rvTutors)

        // Initializing arrays
        imageId = arrayOf(
            R.drawable.si,
            R.drawable.icon,
            R.drawable.ka,
            R.drawable.le,
            R.drawable.icon,
            R.drawable.le,
            R.drawable.th,
            R.drawable.bi,
            R.drawable.ch
        )
        name = arrayOf(
            "Sipho Mthethwa",
            "Zanele Mhlongo",
            "Kabelo Phiri",
            "Lerato Mokoena",
            "Sizwe Nkosi",
            "Mbali Mkhize",
            "Ayanda Ndlovu",
            "Bianca Moodley",
            "Christina Goncalves"
        )
        rating = arrayOf(
            3.9, 4.7, 4.3,4.5, 4.9, 3.8, 4.2, 3.6, 4.95, 4.1
        )
        rate = arrayOf(
            90.0, 180.0, 140.0, 250.0, 100.0, 130.0, 80.0, 300.0, 110.0
        )
        location = arrayOf(
            "Soweto, JHB", "Durban, KZN", "Polokwane, LP",  "Pretoria, GP",
            "Durban, KZN", "Midrand, JHB", "Cape Town, WC", "Stellenbosch, WC", "Randburg, JHB"
        )
        languages = arrayOf(
            "English, Xhosa", "English, Portuguese", "English, Sotho",
            "English, Sotho", "English, Mandarin", "English, Xhosa", "English, Afrikaans",
            "English, Chinese", "English, Zulu"
        )
        description = arrayOf(
            "High school math teacher", "Business and economics tutor",
            "Skilled in computer programming and algorithms","Creative writing specialist",
            "Experienced data scientist", "Tutor with a passion for languages",
            "Skilled in mathematics and physical science", "History and geography enthusiast",
            "Master's in computer engineering", "Biology and environmental science expert"
        )

        // Load aboutTutor from resources (adjust resource names as needed)
        aboutTutor = arrayOf(
            getString(R.string.sipho),
            getString(R.string.zanele),
            getString(R.string.kabelo),
            getString(R.string.lerato),
            getString(R.string.sizwe),
            getString(R.string.mbali),
            getString(R.string.ayanda),
            getString(R.string.bianca),
            getString(R.string.christina)

        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        tutorList = arrayListOf()
        getUserData()

        return view
    }

    private fun getUserData() {
        for (i in imageId.indices) {
            val tutor = Tutor(imageId[i], name[i], rating[i], rate[i], location[i], languages[i], description[i])
            tutorList.add(tutor)
        }

        // Adapter setup
        val adapter = TutorAdapter(tutorList)
        recyclerView.adapter = adapter

        // Set item click listener
        adapter.setOnitemClickListener(object : TutorAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), TutorExpandedActivity::class.java)
                intent.putExtra("ProfilePic", tutorList[position].ProfilePic)
                intent.putExtra("Name", tutorList[position].Name)
                intent.putExtra("Rating", tutorList[position].Rating.toString())
                intent.putExtra("Rate", tutorList[position].Rate.toString())
                intent.putExtra("Location", tutorList[position].Location)
                intent.putExtra("Language", tutorList[position].Language)
                intent.putExtra("Description", tutorList[position].Description)
                intent.putExtra("aboutTutor", aboutTutor[position])
                startActivity(intent)
            }
        })
    }
}
