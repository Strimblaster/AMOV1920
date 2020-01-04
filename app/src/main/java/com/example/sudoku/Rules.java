package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Rules extends AppCompatActivity {
        Button btnBack;
        TextView tvRules;
        @Override
        public void onBackPressed() {
                Intent intent = new Intent(Rules.this, DifficultyView.class);
                intent.putExtra("mode",getIntent().getStringExtra("mode"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                finish();
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_rules);
                btnBack = findViewById(R.id.btnBack);
                tvRules = findViewById(R.id.tvRules);
                tvRules.setText("As regras do sudoku (que em japonês significa número único) são simples e, apesar de apresentar números, não é necessário fazer qualquer tipo de conta. Basta completar todos os espaços seguindo as seguintes restrições:\n" +
                    "\n" +
                    "Não repetir números na mesma linha, na mesma coluna nem na mesma grade 3x3.\n" +
                    "Neste jogo, isso não será um problema, pois o jogo não permite que isso aconteça. Entretanto, isso não quer dizer que não é possível fazer jogadas erradas, já que existem os erros lógicos.\n" +
                    "\n" +
                    "Os erros lógicos são erros que seguem as regras (não existe repetição), mas que deixam o jogo sem jogadas no futuro.\n" +
                    "\n" +
                    "O jogo terminará quando todas as posições estiverem completas ou tenha chegado ao limite de erros.");
                btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(Rules.this, DifficultyView.class);
                                intent.putExtra("mode",getIntent().getStringExtra("mode"));
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                                finish();
                        }
                });

        }
}
