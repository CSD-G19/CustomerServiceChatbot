package com.project.genassist_ecommerce.chatbot

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.project.genassist_ecommerce.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var image: ImageView
    private lateinit var recyclerView: RecyclerView
    private var bitmap: Bitmap? = null
    private lateinit var imageUri: String
    private var responseData = arrayListOf<DataResponse>()

    private lateinit var adapter: GeminiAdapter

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri.toString()
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            image.setImageURI(uri)
        } else {
            Log.d("Photopicker", "No media selected")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        editText = findViewById(R.id.ask_edit_text)
        button = findViewById(R.id.ask_button)
        image = findViewById(R.id.select_iv)
        recyclerView = findViewById(R.id.recycler_view_id)

        adapter = GeminiAdapter(this, responseData)
        recyclerView.adapter = adapter

        image.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        button.setOnClickListener {
            if (editText.text != null) {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = getString(R.string.api_key)
                )

                val userQuery = editText.text.toString()
                editText.setText("")

                // Define Chase's personality prompt
                val chasePrompt = """
                   You are Chase, a witty and empathetic customer support assistant for an e-commerce platform.
                   Your role is to assist users in a friendly and conversational manner, providing helpful responses to their queries. 
                    Key traits:
                    - Be warm, humble, and natural in tone—like a helpful friend, not a robot.
                    - Always vary your responses to avoid sounding repetitive, even for similar queries.
                    - Keep your replies short and clear (under 50 words).
                    - Never ask the user about their situation; respond directly based on the query.
                    
                    Handling issues:
                    - For delays: Attribute them to a relatable issue like a minor vehicle breakdown or a brief technical glitch. Reassure the user calmly and offer a friendly apology.
                    - For wrong deliveries: Mention it as a rare system mix-up, and reassure them that it’ll be fixed promptly.
                    
                    Maintain variety:
                    - Add subtle humor or creativity to responses when appropriate, but prioritize empathy and clarity.
                    - Use different phrasing for similar queries to keep the conversation fresh and engaging.

                """.trimIndent()

                val completePrompt = "$chasePrompt\nUser Query: $userQuery"

                if (bitmap != null) {
                    responseData.add(DataResponse(0, userQuery, imageUri = imageUri))
                    adapter.notifyDataSetChanged()

                    val inputContent = content {
                        image(bitmap!!)
                        text(completePrompt)
                    }

                    GlobalScope.launch {
                        val response = generativeModel.generateContent(inputContent)

                        runOnUiThread {
                            responseData.add(
                                DataResponse(
                                    1,
                                    response.text ?: "I'm here to assist!",
                                    ""
                                )
                            )
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    responseData.add(DataResponse(0, userQuery, ""))
                    adapter.notifyDataSetChanged()

                    GlobalScope.launch {
                        bitmap = null
                        imageUri = ""
                        val response = generativeModel.generateContent(completePrompt)
                        runOnUiThread {
                            responseData.add(
                                DataResponse(
                                    1,
                                    response.text ?: "I'm here to assist!",
                                    ""
                                )
                            )
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}
