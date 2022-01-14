(ns solana-clj.spl-token
  (:require
   ["@solana/spl-token"     :as spl-token]
   ["@solana/web3.js"       :as sol]
   [cljs-bean.core          :refer [->clj bean ->js]]
   [cljs.core.async.interop :refer [<p!]]
   [cljs.core.async         :as    a
                            :refer [go <!]]))

(def ^js AccountLayout (bean spl-token/AccountLayout))
(def ^js MintLayout (bean spl-token/MintLayout))

(def ^js Token spl-token/Token)

(def ^:const token-program-id spl-token/TOKEN_PROGRAM_ID)
(def ^:const associated-token-program-id spl-token/ASSOCIATED_TOKEN_PROGRAM_ID)
(def ^:const native-mint spl-token/NATIVE_MINT)

;; Authority types
(def ^:const authority-type-mint-tokens "MintTokens")
(def ^:const authority-type-freeze-account "FreezeAccount")
(def ^:const authority-type-account-owner "AccountOwner")
(def ^:const authority-type-close-account "CloseAccount")

(defn create-init-account-instruction
  [^sol/Pukbey program-id ^sol/Pubkey mint ^sol/Pubkey account ^sol/Pubkey owner]
  (spl-token/Token.createInitAccountInstruction program-id mint account owner))

(defn get-min-balance-rent-for-exempt-mint
  [^sol/Connection conn]
  (go (<p! (spl-token/Token.getMinBalanceRentForExemptMint conn))))

(defn get-min-balance-rent-for-exempt-account
  [^sol/Connection conn]
  (go (<p! (spl-token/Token.getMinBalanceRentForExemptAccount conn))))

(defn get-min-balance-rent-for-exempt-multisig
  [^sol/Connection conn]
  (go (<p! (spl-token/Token.getMinBalanceRentForExemptMultisig conn))))

(defn get-associated-token-address
  "Get the address for the associated token account.

  associated-program-id: SPL Associated Token program account
  program-id: SPL Token program account
  mint: Token mint account
  owner: Owner of the new account"
  [associated-program-id program-id mint owner & [allow-owner-off-curve]]
  (go (<p! (spl-token/Token.getAssociatedTokenAddress
            associated-program-id
            program-id
            mint
            owner
            (true? allow-owner-off-curve)))))

(defn create-associated-token-account-instruction
  [associated-program-id program-id mint associated-account owner payer]
  (spl-token/Token.createAssociatedTokenAccountInstruction
   associated-program-id
   program-id
   mint
   associated-account
   owner
   payer))

(defn create-transfer-instruction
  [program-id source destination owner multi-signers amount]
  (spl-token/Token.createTransferInstruction
   program-id
   source
   destination
   owner
   (->js multi-signers)
   amount))
